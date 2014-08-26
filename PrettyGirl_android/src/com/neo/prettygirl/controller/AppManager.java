package com.neo.prettygirl.controller;

import android.app.Application;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.neo.prettygirl.PGApplication;

/**
 * @author LiuBing
 * @version 2014-3-7 下午2:36:04
 */
public class AppManager extends BaseManager implements UpdatePointsNotifier{
	private static AppManager mInstance;
	public int coin;

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
		AppConnect.getInstance(PGApplication.getContext()); 
		AppConnect.getInstance(PGApplication.getContext()).getPoints(this);
//		AppConnect.getInstance(PGApplication.getContext()).showOffers(this);
//		AppConnect.getInstance(PGApplication.getContext()).showTuanOffers(this);
		AppConnect.getInstance(PGApplication.getContext()).setCrashReport(true);
		ImageDataManager.getInstance();
		NetServiceManager.getInstance();
	}
	//AppConnect.getInstance(this).spendPoints(int amount, this);

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
		
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
