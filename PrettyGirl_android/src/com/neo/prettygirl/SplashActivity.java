package com.neo.prettygirl;

import java.util.Timer;
import java.util.TimerTask;

import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.controller.NetServiceManager;
import com.neo.prettygirl.event.BroadCastEvent;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initUI();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	private void initUI() {
		ImageDataManager.getInstance();
		timer = new Timer();
		timer.schedule(new SplashTask(), 3 * 1000);
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
