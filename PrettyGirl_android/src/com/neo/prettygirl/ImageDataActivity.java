package com.neo.prettygirl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.controller.NetServiceManager;
import com.neo.prettygirl.event.BroadCastEvent;
import com.neo.prettygirl.fragment.PGImageDataFragment;

import de.greenrobot.event.EventBus;

public class ImageDataActivity extends FragmentActivity {
	public static String res_id;
	private PGImageDataFragment imageDataFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		EventBus.getDefault().register(this, BroadCastEvent.class);
		setContentView(R.layout.activity_data);
		initUI();
	}

	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
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
	}

	private void initData() {
		res_id = getIntent().getExtras().getString("res_id");
		ImageDataManager.getInstance().getDBResIdImageData(res_id);
		NetServiceManager.getInstance().getResImageListData(res_id);// 第一页
	}
}
