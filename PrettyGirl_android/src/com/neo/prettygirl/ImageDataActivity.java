package com.neo.prettygirl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.neo.prettygirl.controller.NetServiceManager;
import com.neo.prettygirl.event.BroadCastEvent;
import com.neo.prettygirl.fragment.PGMainFragment;
import com.neo.prettygirl.fragment.PGMyFragment;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

public class ImageDataActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, BroadCastEvent.class);
		setContentView(R.layout.activity_main);	
		initUI();
		initData();
	}

	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.GET_ALL_IMAGE_LIST_DATA:
			break;
		default:
			break;
		}
	}

	private void initUI() {

	}

	private void initData() {
//		NetServiceManager.getInstance().getResImageListData(0);// 第一页
	}
}
