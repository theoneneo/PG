package com.neo.pgapp;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.neo.pgapp.controller.AppManager;
import com.neo.pgapp.controller.ImageDataManager;

public class SplashActivity extends BaseActivity {
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.activity_splash);
		initUI();
	}

	protected void onDestroy() {
		super.onDestroy();
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
		timer = new Timer();
		timer.schedule(new SplashTask(), 2 * 1000);
	}

	private void go2MainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	class SplashTask extends TimerTask {
		public void run() {
			timer.cancel(); // Terminate the timer thread
			go2MainActivity();
		}
	}
}
