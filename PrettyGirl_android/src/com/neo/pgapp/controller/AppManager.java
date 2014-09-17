package com.neo.pgapp.controller;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.impl.FileNameRuleImageUrl;
import cn.trinea.android.common.service.impl.ImageSDCardCache;
import cn.trinea.android.common.service.impl.RemoveTypeLastUsedTimeFirst;
import cn.trinea.android.common.service.impl.ImageSDCardCache.OnImageSDCallbackListener;
import cn.trinea.android.common.util.ObjectUtils;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.neo.pgapp.PGApplication;
import com.neo.pgapp.R;

/**
 * @author LiuBing
 * @version 2014-3-7 下午2:36:04
 */
public class AppManager extends BaseManager{
	private static AppManager mInstance;
	public static String APP_PID = "default";
	public int coin;
	public static int width, height;
	public static int curVersion;
	public static String updateLink = null;
	public static boolean isOpen = true;

	private static int IMAGE_MAX_WIDTH = 480;
	private static int IMAGE_MAX_HEIGHT = 800;

	public static final String TAG_CACHE = "image_sdcard_cache";
	/** cache folder path which be used when saving images **/
	public static final String DEFAULT_CACHE_FOLDER = new StringBuilder()
			.append(Environment.getExternalStorageDirectory().getAbsolutePath())
			.append(File.separator).append("Welfare").toString();
	public static final ImageSDCardCache IMAGE_SD_CACHE = new ImageSDCardCache();

	private AppManager(Application app) {
		super(app);
		// TODO Auto-generated constructor stub
		initManager();
	}

	public static AppManager getInstance() {
		AppManager instance;
		if (mInstance == null) {
			synchronized (AppManager.class) {
				if (mInstance == null) {
					instance = new AppManager(PGApplication.getApplication());
					mInstance = instance;
				}
			}
		}
		return mInstance;
	}

	@Override
	protected void initManager() {
		IMAGE_SD_CACHE.initData(PGApplication.getContext(), TAG_CACHE);
		IMAGE_SD_CACHE.setContext(PGApplication.getContext());
		IMAGE_SD_CACHE.setCacheFolder(DEFAULT_CACHE_FOLDER);

		// TODO Auto-generated method stub
		ImageDataManager.getInstance();
		NetServiceManager.getInstance();
	}

	@Override
	public void DestroyManager() {
		// TODO Auto-generated method stub
		IMAGE_SD_CACHE.saveDataToDb(PGApplication.getContext(), TAG_CACHE);
		NetServiceManager.getInstance().DestroyManager();
		ImageDataManager.getInstance().DestroyManager();
	}

	static {
		/** init image cache **/
		OnImageSDCallbackListener imageCallBack = new OnImageSDCallbackListener() {

			/**
			 * callback function after get image successfully, run on ui thread
			 * 
			 * @param imageUrl
			 *            imageUrl
			 * @param imagePath
			 *            image path
			 * @param view
			 *            view need the image
			 * @param isInCache
			 *            whether already in cache or got realtime
			 */
			@Override
			public void onGetSuccess(String imageUrl, String imagePath,
					View view, boolean isInCache) {

				if (view != null && imagePath != null) {
					ImageView imageView = (ImageView) view;
					String imageUrlTag = (String) imageView.getTag();
					if (ObjectUtils.isEquals(imageUrlTag, imageUrl)) {
						BitmapFactory.Options option = new BitmapFactory.Options();
						option.inSampleSize = getImageScale(imagePath);
						Bitmap bm = BitmapFactory.decodeFile(imagePath, option);
						if (bm != null) {
							imageView.setImageBitmap(bm);
							// first time show with animation
							if (!isInCache) {
								imageView.startAnimation(getInAlphaAnimation(2000));
							}
							imageView.setScaleType(ScaleType.FIT_CENTER);
							imageView.setAdjustViewBounds(true);
						}
					}	
				}
			}

			/**
			 * callback function before get image, run on ui thread
			 * 
			 * @param imageUrl
			 *            imageUrl
			 * @param view
			 *            view need the image
			 */
			@Override
			public void onPreGet(String imageUrl, View view) {
				// Log.e(TAG_CACHE, "pre get image");
			}

			@Override
			public void onGetNotInCache(String imageUrl, View view) {
				// you can do something when image not in cache, for example set
				// default image
				if (view != null && view instanceof ImageView) {
					((ImageView) view).setImageResource(R.drawable.empty_photo);
					((ImageView) view).setScaleType(ScaleType.CENTER);
				}
			}

			/**
			 * callback function after get image failed, run on ui thread
			 * 
			 * @param imageUrl
			 *            imageUrl
			 * @param imagePath
			 *            image path
			 * @param view
			 *            view need the image
			 * @param failedReason
			 *            failed reason for get image
			 */
			@Override
			public void onGetFailed(String imageUrl, String imagePath,
					View view, FailedReason failedReason) {
				Log.e(TAG_CACHE,
						new StringBuilder(128).append("get image ")
								.append(imageUrl)
								.append(" error, failed type is: ")
								.append(failedReason.getFailedType())
								.append(", failed reason is: ")
								.append(failedReason.getCause().getMessage())
								.toString());
			}
		};
		IMAGE_SD_CACHE.setOnImageSDCallbackListener(imageCallBack);
		IMAGE_SD_CACHE
				.setCacheFullRemoveType(new RemoveTypeLastUsedTimeFirst<String>());
		IMAGE_SD_CACHE.setFileNameRule(new FileNameRuleImageUrl());

		IMAGE_SD_CACHE.setHttpReadTimeOut(10000);
		IMAGE_SD_CACHE.setOpenWaitingQueue(true);
		IMAGE_SD_CACHE.setValidTime(-1);
	}

	/**
	 * scale image to fixed height and weight
	 * 
	 * @param imagePath
	 * @return
	 */
	private static int getImageScale(String imagePath) {
		BitmapFactory.Options option = new BitmapFactory.Options();

		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, option);

		int scale = 1;
		while (option.outWidth / scale >= IMAGE_MAX_WIDTH
				|| option.outHeight / scale >= IMAGE_MAX_HEIGHT) {
			scale *= 2;
		}
		return scale;
	}

	public static AlphaAnimation getInAlphaAnimation(long durationMillis) {
		AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
		inAlphaAnimation.setDuration(durationMillis);
		return inAlphaAnimation;
	}

	private static Bitmap makeBitmap(String imagePath) {
		float width = 0;
		float height = 0;
		BitmapFactory.Options option = new BitmapFactory.Options();
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, option);

		float scale = 0;
		if (new Float(option.outWidth) / new Float(option.outHeight) <= new Float(
				AppManager.width) / new Float(AppManager.height)) {
			scale = new Float(AppManager.height) / new Float(option.outHeight);
		} else {
			scale = new Float(AppManager.width) / new Float(option.outWidth);
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		return resizeBmp;
	}
}
