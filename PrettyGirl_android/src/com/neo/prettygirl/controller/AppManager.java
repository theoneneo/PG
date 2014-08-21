package com.neo.prettygirl.controller;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.view.WindowManager.LayoutParams;
import com.neo.prettygirl.PGApplication;

/**
 * @author LiuBing
 * @version 2014-3-7 下午2:36:04
 */
public class AppManager extends BaseManager {
	private static AppManager mInstance;

	private AppManager(Application app) {
		super(app);
		// TODO Auto-generated constructor stub
		initManager();
	}

	public static AppManager getInstance() {
		AppManager instance;
		if (mInstance == null) {
			synchronized (AppManager.class) {
				if (mInstance == null) {
					instance = new AppManager(PGApplication.getApplication());
					mInstance = instance;
				}
			}
		}
		return mInstance;
	}

	@Override
	protected void initManager() {
		// TODO Auto-generated method stub
		ImageDataManager.getInstance();
		NetServiceManager.getInstance();
	}

	@Override
	public void DestroyManager() {
		// TODO Auto-generated method stub
		NetServiceManager.getInstance().DestroyManager();
		ImageDataManager.getInstance().DestroyManager();
	}
}
