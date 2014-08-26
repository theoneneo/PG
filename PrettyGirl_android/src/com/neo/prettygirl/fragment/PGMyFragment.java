package com.neo.prettygirl.fragment;

import java.io.File;

import me.maxwin.view.XListView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.impl.FileNameRuleImageUrl;
import cn.trinea.android.common.service.impl.ImageSDCardCache;
import cn.trinea.android.common.service.impl.ImageSDCardCache.OnImageSDCallbackListener;
import cn.trinea.android.common.service.impl.RemoveTypeLastUsedTimeFirst;

import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.neo.prettygirl.ImageDataActivity;
import com.neo.prettygirl.PGApplication;
import com.neo.prettygirl.R;
import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.db.DBTools;

public class PGMyFragment extends BaseFragment {
	private XListView mAdapterView = null;
	private MyImageAdapter mAdapter = null;
	private int page = 0;
	private static int IMAGE_MAX_WIDTH = 480;
	private static int IMAGE_MAX_HEIGHT = 800;

	public static final String TAG_CACHE = "image_sdcard_cache";
	/** cache folder path which be used when saving images **/
	public static final String DEFAULT_CACHE_FOLDER = new StringBuilder()
			.append(Environment.getExternalStorageDirectory().getAbsolutePath())
			.append(File.separator).append("Welfare").append(File.separator)
			.append("main_image_cache").toString();
	public static final ImageSDCardCache IMAGE_SD_CACHE = new ImageSDCardCache();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IMAGE_SD_CACHE.initData(PGApplication.getContext(), TAG_CACHE);
		IMAGE_SD_CACHE.setContext(PGApplication.getContext());
		IMAGE_SD_CACHE.setCacheFolder(DEFAULT_CACHE_FOLDER);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_multilist, null);
		initView(v);
		return v;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void initView(View v) {
		mAdapterView = (XListView) v.findViewById(R.id.list);
		mAdapterView.setPullRefreshEnable(false);
		mAdapterView.setPullLoadEnable(false);
		mAdapter = new MyImageAdapter(getActivity());
		mAdapterView.setAdapter(mAdapter);
		mAdapterView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				buyImageData(position);
			}
		});
	}

	private void buyImageData(int position) {
		int len = mAdapter.getCount();
		String res_id = ImageDataManager.getInstance().mainGroupImage.imageData
				.get(len - position).res_id;
		go2ImageDataActivity(res_id);
	}

	public void updateMainAdapter() {
		mAdapter.notifyDataSetChanged();
	}

	private class MyImageAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public MyImageAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = (View) inflater.inflate(R.layout.item_grid,
						parent, false);
				holder = new ViewHolder();
				holder.row_image = (ImageView) convertView
						.findViewById(R.id.row_image);
				holder.row_text = (TextView) convertView
						.findViewById(R.id.row_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			int len = getCount();
			IMAGE_SD_CACHE.get(
					ImageDataManager.getInstance().myGroupImage.imageData
							.get(len - position - 1).link, holder.row_image);
			holder.row_text
					.setText(ImageDataManager.getInstance().myGroupImage.imageData
							.get(len - position - 1).coin);

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ImageDataManager.getInstance().myGroupImage.imageData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	private class ViewHolder {
		ImageView row_image;
		TextView row_text;
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

				// avoid oom caused by bitmap size exceeds VM budget
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

	public static AlphaAnimation getInAlphaAnimation(long durationMillis) {
		AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
		inAlphaAnimation.setDuration(durationMillis);
		return inAlphaAnimation;
	}
	
	private void go2ImageDataActivity(String res_id){
		Intent intent = new Intent(getActivity(), ImageDataActivity.class);
		intent.putExtra("res_id", res_id);
		startActivity(intent);
	}
}
