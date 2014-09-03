package com.neo.pgapp;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.neo.pgapp.R;
import com.neo.pgapp.controller.AppManager;
import com.neo.pgapp.controller.ImageDataManager;
import com.neo.pgapp.data.ImageResDataStruct;

public class ImageActivity extends BaseActivity implements OnPageChangeListener {
	private int position;
	private ViewPager viewPager;
	private ImageView[] mImageViews;

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
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void initUI() {
		// 将图片装载到数组中
		mImageViews = new ImageView[ImageDataManager.getInstance().curGroupImage.imageData
				.size()];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			ImageResDataStruct data = ImageDataManager.getInstance().curGroupImage.imageData
					.get(i);
			imageView.setTag(data.link);
			AppManager.IMAGE_SD_CACHE.get(data.link, imageView);
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		// 设置Adapter
		viewPager.setAdapter(new ImageAdapter());
		// 设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(this);
		// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
		viewPager.setCurrentItem(position);
	}

	private void initData() {
		position = getIntent().getExtras().getInt("position");
	}

	public class ImageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return ImageDataManager.getInstance().curGroupImage.imageData
					.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mImageViews[position
					% mImageViews.length]);

		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(mImageViews[position
					% mImageViews.length], 0);
			return mImageViews[position % mImageViews.length];
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}
}
