package com.neo.pgapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.neo.mtapp.R;
import com.neo.pgapp.controller.AppManager;
import com.neo.pgapp.controller.ImageDataManager;
import com.neo.pgapp.controller.NetServiceManager;
import com.neo.pgapp.event.BroadCastEvent;

import de.greenrobot.event.EventBus;

public class SplashActivity extends BaseActivity {
	private boolean isUpdate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, BroadCastEvent.class);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.activity_splash);
		initUI();
	}

	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.GET_UPDATE_APK:
			if (AppManager.updateLink != null && (!"".equals(AppManager.updateLink)))
				isUpdate = true;
			go2MainActivity();
			break;
		default:
			break;
		}
	}

	private void initUI() {
		ApplicationInfo appInfo = null;
		try {
			appInfo = this.getPackageManager()  
	                .getApplicationInfo(getPackageName(),   
	                        PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(appInfo != null)
			AppManager.APP_PID = appInfo.metaData.getString("APP_PID");
		
		DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		AppManager.width = dm.widthPixels;
		AppManager.height = dm.heightPixels;
		ImageDataManager.getInstance();
		
		update();
	}

	private void go2MainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("isupdate", isUpdate);
		startActivity(intent);
		finish();
	}

	private void update(){
		if (isNetworkConnected(this)) {
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
			NetServiceManager.getInstance().getUpdateApk(pi.versionCode, AppManager.APP_PID);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
