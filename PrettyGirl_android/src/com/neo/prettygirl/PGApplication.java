package com.neo.prettygirl;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.neo.prettygirl.controller.AppManager;

public class PGApplication extends Application {
	private static Application app;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		AppManager.getInstance();
	}

	public void onTerminate() {
		super.onTerminate();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public static Application getApplication() {
		return app;
	}

	public static Context getContext() {
		return getApplication().getApplicationContext();
	}

	// TODO activity 界面管理 liubing====
	private static ArrayList<Activity> list = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		list.add(activity);
	}

	public static void exit() {
		for (Activity activity : list) {
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
