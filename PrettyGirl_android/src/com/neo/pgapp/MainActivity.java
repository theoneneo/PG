package com.neo.pgapp;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
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

import com.neo.mtapp.R;
import com.neo.pgapp.controller.AppManager;
import com.neo.pgapp.controller.ImageDataManager;
import com.neo.pgapp.controller.NetServiceManager;
import com.neo.pgapp.event.BroadCastEvent;
import com.neo.pgapp.fragment.PGMainFragment;
import com.neo.pgapp.fragment.PGMyFragment;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity{
	private static final String[] CONTENT = new String[] { "精彩福利", "我的福利" };

	private boolean isUpdate = false;
	private MainAdapter adapter;
	private PGMainFragment mainListFragment;
	private PGMyFragment myListFragment;
	private View slidingDrawerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, BroadCastEvent.class);
		setContentView(R.layout.activity_main);		
		isUpdate = getIntent().getExtras().getBoolean("isupdate");
		initUI();
		initData();
		updateFunction();
	}

	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		AppManager.getInstance().DestroyManager();
		super.onBackPressed();
	}

	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.GET_MAIN_IMAGE_LIST_DATA:
			if (mainListFragment != null)
				mainListFragment.updateMainAdapter();
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
	}

	private void initData() {

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
							.size(), AppManager.APP_PID);// 第一页
			
			if(isUpdate)
				popUpdateWindow();
		} else {
			Toast.makeText(this, R.string.net_error, Toast.LENGTH_LONG).show();
		}
	}

	private void popUpdateWindow() {
		// 升级提示框
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setCancelable(false);  
        builder.setMessage("有新版本请升级");
        builder.setPositiveButton("确认",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    	downloadApk();  
                    }  
                });  
        builder.setNegativeButton("取消",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) { 
                    }  
                });  
        builder.show(); 
	}
	
	private void downloadApk(){
		DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

		Uri uri = Uri.parse(AppManager.updateLink);
		Request request = new Request(uri);

		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);

		request.setVisibleInDownloadsUi(true);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir("Welfare", "PrettyGirl.apk");
		downloadManager.enqueue(request);
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
	
	private void updateFunction(){
		if(AppManager.isOpen){
		}else{
			
		}
	}
}
