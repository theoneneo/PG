package com.neo.prettygirl;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.impl.FileNameRuleImageUrl;
import cn.trinea.android.common.service.impl.ImageSDCardCache;
import cn.trinea.android.common.service.impl.ImageSDCardCache.OnImageSDCallbackListener;
import cn.trinea.android.common.service.impl.RemoveTypeLastUsedTimeFirst;

import com.neo.prettygirl.controller.AppManager;
import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.data.GroupImageResDataStruct;
import com.neo.prettygirl.data.ImageResDataStruct;
import com.neo.prettygirl.tools.ImageEraserView;

public class ImageActivity extends BaseActivity {
	private int position;
	private String parent_res_id;
	private ImageView image;
	private ImageEraserView mFloatImage;
	private ImageResDataStruct data;
	private boolean mCanTouch = false;
	private ImageButton right_btn, left_btn;

	private static int IMAGE_MAX_WIDTH = 480;
	private static int IMAGE_MAX_HEIGHT = 800;
	public static final String TAG_CACHE = "image_sdcard_cache";
	/** cache folder path which be used when saving images **/
	public String DEFAULT_CACHE_FOLDER;
	public static final ImageSDCardCache IMAGE_SD_CACHE = new ImageSDCardCache();

	private boolean mIsAnimRunning = false;
	private final int mEarseRate = 70;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.activity_image);
		initData();
		initUI();
	}

	@Override
	public void onDestroy() {
		IMAGE_SD_CACHE.saveDataToDb(PGApplication.getContext(), TAG_CACHE);
		super.onDestroy();
	}

	private void initUI() {
		mFloatImage = (ImageEraserView) this.findViewById(R.id.earseview);
		mFloatImage.prepare(this, R.drawable.bbg);
		mFloatImage
				.setOnDrawAreaRateCallback(new ImageEraserView.OnDrawAreaRateCallback() {
					@Override
					public void setRate(float rate) {
						if (rate >= mEarseRate && !mIsAnimRunning) {
							Animation animation = AnimationUtils.loadAnimation(
									ImageActivity.this, R.anim.alpha_out);
							animation
									.setAnimationListener(new Animation.AnimationListener() {

										@Override
										public void onAnimationStart(
												Animation animation) {
											mIsAnimRunning = true;
										}

										@Override
										public void onAnimationRepeat(
												Animation animation) {

										}

										@Override
										public void onAnimationEnd(
												Animation animation) {
											mIsAnimRunning = false;
											mFloatImage
													.setVisibility(View.GONE);
											mCanTouch = true;
										}
									});
							mFloatImage.startAnimation(animation);
						}
					}
				});

		image = (ImageView) findViewById(R.id.image);
		IMAGE_SD_CACHE.get(data.link, image);

		left_btn = (ImageButton) findViewById(R.id.left_btn);
		left_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeImageData(--position);
			}
		});

		right_btn = (ImageButton) findViewById(R.id.right_btn);
		right_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeImageData(++position);
			}
		});

		showLeftRightButton();
	}

	private void initData() {
		parent_res_id = getIntent().getExtras().getString("parent_res_id");
		DEFAULT_CACHE_FOLDER = new StringBuilder()
				.append(Environment.getExternalStorageDirectory()
						.getAbsolutePath()).append(File.separator)
				.append("Welfare").append(File.separator)
				.append("all_image_cache").append(File.separator)
				.append(parent_res_id).toString();

		position = getIntent().getExtras().getInt("position");
		GroupImageResDataStruct dd = ImageDataManager.getInstance().curGroupImage;
		data = dd.imageData.get(position);

		IMAGE_SD_CACHE.initData(PGApplication.getContext(), TAG_CACHE);
		IMAGE_SD_CACHE.setContext(PGApplication.getContext());
		IMAGE_SD_CACHE.setCacheFolder(DEFAULT_CACHE_FOLDER);
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
				ImageView imageView = (ImageView) view;
				Bitmap bm = makeBitmap(imagePath);
				if (bm != null) {
					imageView.setImageBitmap(bm);

					// first time show with animation
					if (!isInCache) {
						imageView.startAnimation(getInAlphaAnimation(2000));
					}
					imageView.setScaleType(ScaleType.FIT_CENTER);
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
					((ImageView) view).setImageResource(R.drawable.ic_launcher);
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

	private static Bitmap makeBitmap(String imagePath) {
		float width = 0;
		float height = 0;
		BitmapFactory.Options option = new BitmapFactory.Options();
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, option);

		float scale = 0;
		if (new Float(option.outWidth) / new Float(option.outHeight) <= new Float(
				AppManager.width) / new Float(AppManager.height)) {
			scale =  new Float(AppManager.height) / new Float(
					option.outHeight);
		} else {
			scale = new Float(AppManager.width) / new Float(
					option.outWidth);
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		return resizeBmp;
	}

	public static AlphaAnimation getInAlphaAnimation(long durationMillis) {
		AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
		inAlphaAnimation.setDuration(durationMillis);
		return inAlphaAnimation;
	}

	private void showLeftRightButton() {
		if (position == 0) {
			left_btn.setVisibility(View.GONE);
		} else {
			left_btn.setVisibility(View.VISIBLE);
		}

		if (position == ImageDataManager.getInstance().curGroupImage.imageData
				.size() - 1) {
			right_btn.setVisibility(View.GONE);
		} else {
			right_btn.setVisibility(View.VISIBLE);
		}
	}

	private void changeImageData(int position) {
		showLeftRightButton();
		mFloatImage.prepare(this, R.drawable.ic_launcher);
		GroupImageResDataStruct dd = ImageDataManager.getInstance().curGroupImage;
		data = dd.imageData.get(position);
		IMAGE_SD_CACHE.get(data.link, image);
	}
}
