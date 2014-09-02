package com.neo.prettygirl;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.Toast;
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
		AppConnect.getInstance(this).close();
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
		AppManager.getInstance().DestroyManager();
		super.onBackPressed();
	}

	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.GET_MAIN_IMAGE_LIST_DATA:
			if (mainListFragment != null)
				mainListFragment.updateMainAdapter();
			break;
		case BroadCastEvent.GET_UPDATE_APK:
			if (AppManager.updateLink != null)
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

		ImageButton rightBtn = (ImageButton) findViewById(R.id.title)
				.findViewById(R.id.right_btn);
		rightBtn.setVisibility(View.INVISIBLE);
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

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
		AppConnect.getInstance("20dba03620b3cb908557e6b6fdb87148", "APP_PID",
				this);
		// AppConnect.getInstance(this). initUninstallAd(this);
		AppManager.getInstance().getPoint(this);
		AppConnect.getInstance(this).initAdInfo();
		AppConnect.getInstance(this).initPopAd(PGApplication.getContext());

		if (isNetworkConnected(this)) {
			if (!isToastRefresh()) {
				Toast.makeText(this, R.string.down_refresh, Toast.LENGTH_LONG)
						.show();
				SharedPreferences settings = getSharedPreferences("pgapp", 0);
				SharedPreferences.Editor localEditor = settings.edit();
				localEditor.putBoolean("toast_refresh", true);
				localEditor.commit();
			}

			NetServiceManager.getInstance().getMainImageListData(
					ImageDataManager.getInstance().mainGroupImage.imageData
							.size());// 第一页
			updateApk();
		} else {
			Toast.makeText(this, R.string.net_error, Toast.LENGTH_LONG).show();
		}
	}

	private void updateApk() {
		PackageInfo pi;
		try {
			pi = getPackageManager().getPackageInfo(this.getPackageName(), 0);
			AppManager.curVersion = pi.versionCode;
			NetServiceManager.getInstance().getUpdateApk(pi.versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void popUpdateWindow() {
		// 升级提示框
		DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

		Uri uri = Uri.parse("fileUrl");
		Request request = new Request(uri);

		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);

		// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
		// request.setShowRunningNotification(false);

		// 不显示下载界面
		request.setVisibleInDownloadsUi(true);
		request.setDestinationInExternalPublicDir("Welfare", "PrettyGirl.apk");
		long id = downloadManager.enqueue(request);
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

	private boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	private boolean isToastRefresh() {
		SharedPreferences settings = getSharedPreferences("pgapp", 0);
		return settings.getBoolean("toast_refresh", false);
	}
}
