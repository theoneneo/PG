package com.neo.prettygirl.controller;

import android.app.Application;
import android.util.DisplayMetrics;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.neo.prettygirl.PGApplication;

/**
 * @author LiuBing
 * @version 2014-3-7 下午2:36:04
 */
public class AppManager extends BaseManager implements UpdatePointsNotifier {
	private static AppManager mInstance;
	public int coin;
	public static int width, height;
	public static int curVersion;
	public static String updateLink = null;

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
		AppConnect.getInstance("20dba03620b3cb908557e6b6fdb87148", "APP_PID",
				PGApplication.getContext());
		// AppConnect.getInstance(this). initUninstallAd(this);
		AppConnect.getInstance(PGApplication.getContext()).initAdInfo();
		AppConnect.getInstance(PGApplication.getContext()).initPopAd(
				PGApplication.getContext());
		AppConnect.getInstance(PGApplication.getContext()).getPoints(this);
		AppConnect.getInstance(PGApplication.getContext()).setCrashReport(true);
		AppConnect.getInstance(PGApplication.getContext()).awardPoints(1000,
				this);
		AppConnect.getInstance(PGApplication.getContext()).spendPoints(10000,
				this);
		ImageDataManager.getInstance();
		NetServiceManager.getInstance();
	}

	@Override
	public void DestroyManager() {
		// TODO Auto-generated method stub
		AppConnect.getInstance(PGApplication.getContext()).close();

		NetServiceManager.getInstance().DestroyManager();
		ImageDataManager.getInstance().DestroyManager();
	}

	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		// TODO Auto-generated method stub
		coin = arg1;
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub

	}
}
