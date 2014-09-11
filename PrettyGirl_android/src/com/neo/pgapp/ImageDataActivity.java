package com.neo.pgapp;

import java.io.File;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.impl.FileNameRuleImageUrl;
import cn.trinea.android.common.service.impl.ImageSDCardCache;
import cn.trinea.android.common.service.impl.RemoveTypeLastUsedTimeFirst;
import cn.trinea.android.common.service.impl.ImageSDCardCache.OnImageSDCallbackListener;
import cn.waps.AppConnect;

import com.neo.pgapp.R;
import com.neo.pgapp.controller.ImageDataManager;
import com.neo.pgapp.controller.NetServiceManager;
import com.neo.pgapp.data.ImageResDataStruct;
import com.neo.pgapp.event.BroadCastEvent;
import com.neo.pgapp.fragment.PGImageDataFragment;
import com.neo.pgapp.waps.SlideWall;

import de.greenrobot.event.EventBus;

public class ImageDataActivity extends BaseActivity {
	public static String res_id;
	private ImageResDataStruct curRes;
	private PGImageDataFragment imageDataFragment;
	private View slidingDrawerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		EventBus.getDefault().register(this, BroadCastEvent.class);
		setContentView(R.layout.activity_data);
		initUI();

		AppConnect.getInstance(this).showPopAd(this);
	}

	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (SlideWall.getInstance().slideWallDrawer != null
				&& SlideWall.getInstance().slideWallDrawer.isOpened()) {
			// 如果抽屉式应用墙展示中，则关闭抽屉
			SlideWall.getInstance().closeSlidingDrawer();
			return;
		}
		super.onBackPressed();
	}

	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.GET_ALL_IMAGE_LIST_DATA:
			imageDataFragment.updateAdapter();
			break;
		default:
			break;
		}
	}

	private void initUI() {
		imageDataFragment = (PGImageDataFragment) getSupportFragmentManager()
				.findFragmentById(R.id.list);

		ImageButton leftBtn = (ImageButton) findViewById(R.id.title)
				.findViewById(R.id.left_btn);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView titleText = (TextView) findViewById(R.id.title).findViewById(
				R.id.title_text);
		titleText.setText(R.string.app_name);

		slidingDrawerView = SlideWall.getInstance().getView(this);
		if (slidingDrawerView != null) {
			addContentView(slidingDrawerView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}

	private void initData() {
		res_id = getIntent().getExtras().getString("res_id");
		for (int i = 0; i < ImageDataManager.getInstance().mainGroupImage.imageData
				.size(); i++) {
			ImageResDataStruct data = ImageDataManager.getInstance().mainGroupImage.imageData
					.get(i);
			if (data.res_id.equals(res_id)) {
				curRes = data;
				break;
			}
		}

		ImageDataManager.getInstance().getDBResIdImageData(res_id);
		NetServiceManager.getInstance().getResImageListData(res_id);// 第一页
	}
}
