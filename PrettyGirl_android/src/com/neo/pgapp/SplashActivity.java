package com.neo.pgapp;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import cn.waps.AppConnect;

import com.neo.pgapp.R;
import com.neo.pgapp.controller.AppManager;
import com.neo.pgapp.controller.ImageDataManager;
import com.neo.pgapp.controller.NetServiceManager;

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
