package com.neo.pgapp.controller;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author LiuBing
 * @version 2014-2-17 下午2:36:04
 */
public abstract class BaseManager {
	protected static Application mApp;
	protected static Context mContext;
	protected static SharedPreferences mSharedPreferences;

	protected BaseManager(Application app) {
		mApp = app;
		mContext = app.getApplicationContext();
		mSharedPreferences = mContext.getSharedPreferences("SharedPreferences",
				0);
	}

	public static Context getContext() {
		return mContext;
	}

	public static SharedPreferences getSharedPreferences() {
		return mSharedPreferences;
	}

	protected abstract void initManager();

	public abstract void DestroyManager();
}
