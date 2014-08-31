package com.neo.prettygirl;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.waps.AppConnect;

import com.neo.prettygirl.controller.AppManager;
import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.controller.NetServiceManager;
import com.neo.prettygirl.event.BroadCastEvent;
import com.neo.prettygirl.fragment.PGMainFragment;
import com.neo.prettygirl.fragment.PGMyFragment;
import com.neo.prettygirl.waps.SlideWall;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {
	private static final String[] CONTENT = new String[] { "美女福利", "我的福利" };

	private MainAdapter adapter;
	private PGMainFragment mainListFragment;
	private PGMyFragment myListFragment;
	private View slidingDrawerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, BroadCastEvent.class);
		setContentView(R.layout.activity_main);
		initUI();
		initData();
	}

	protected void onDestroy() {
		AppManager.getInstance().DestroyManager();
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		if(SlideWall.getInstance().slideWallDrawer != null
				&& SlideWall.getInstance().slideWallDrawer.isOpened()){				
			// 如果抽屉式应用墙展示中，则关闭抽屉
			SlideWall.getInstance().closeSlidingDrawer();
			return;
		}
		super.onBackPressed();
	}

	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.GET_MAIN_IMAGE_LIST_DATA:
			if(mainListFragment != null)
				mainListFragment.updateMainAdapter();
			break;
		case BroadCastEvent.GET_UPDATE_APK:
			if(AppManager.updateLink != null)
				popUpdateWindow();
			break;
		default:
			break;
		}
	}

	private void initUI() {
		adapter = new MainAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		
		ImageButton rightBtn = (ImageButton)findViewById(R.id.title).findViewById(R.id.right_btn);
		rightBtn.setVisibility(View.INVISIBLE);
		rightBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}	
		});
		
		TextView titleText = (TextView)findViewById(R.id.title).findViewById(R.id.title_text);
		titleText.setText(R.string.app_name);
		
    	slidingDrawerView = SlideWall.getInstance().getView(this);
    	if(slidingDrawerView != null){
    		addContentView(slidingDrawerView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	}
	}

	private void initData() {
		NetServiceManager.getInstance().getMainImageListData(ImageDataManager.getInstance().mainGroupImage.imageData.size());// 第一页
		updateApk();
	}
	
	private void updateApk(){
		PackageInfo pi;
		try {  
	        pi=getPackageManager().getPackageInfo(this.getPackageName(), 0);
	        AppManager.curVersion = pi.versionCode;
	        NetServiceManager.getInstance().getUpdateApk(pi.versionCode);
	    } catch (NameNotFoundException e) {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    } 
	}
	
	private void popUpdateWindow(){
		//升级提示框
	}

	class MainAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
		public MainAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				if (mainListFragment == null) {
					mainListFragment = new PGMainFragment();
				}
				return mainListFragment;
			} else if (position == 1) {
				if (myListFragment == null) {
					myListFragment = new PGMyFragment();
				}
				return myListFragment;
			}
			return mainListFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getIconResId(int index) {
			return 0;
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

}
