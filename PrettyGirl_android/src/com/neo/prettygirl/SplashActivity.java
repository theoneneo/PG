package com.neo.prettygirl;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import cn.waps.AppConnect;

import com.neo.prettygirl.controller.AppManager;
import com.neo.prettygirl.controller.ImageDataManager;

public class SplashActivity extends BaseActivity {
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getInstance();

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.activity_splash);
		initUI();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	private void initUI() {
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
