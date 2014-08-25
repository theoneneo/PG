package com.neo.prettygirl.controller;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Application;
import android.database.Cursor;

import com.neo.prettygirl.PGApplication;
import com.neo.prettygirl.data.GroupImageResDataStruct;
import com.neo.prettygirl.data.ImageResDataStruct;
import com.neo.prettygirl.db.DBTools;

import de.greenrobot.event.EventBus;

public class ImageDataManager extends BaseManager {
	private static ImageDataManager mInstance;
	public GroupImageResDataStruct mainGroupImage = new GroupImageResDataStruct();
	public GroupImageResDataStruct myGroupImage = new GroupImageResDataStruct();
	public ArrayList<GroupImageResDataStruct> groupImage = new ArrayList<GroupImageResDataStruct>();

	private ImageDataManager(Application app) {
		super(app);
		// TODO Auto-generated constructor stub
		initManager();
	}

	public static ImageDataManager getInstance() {
		ImageDataManager instance;
		if (mInstance == null) {
			synchronized (ImageDataManager.class) {
				if (mInstance == null) {
					instance = new ImageDataManager(
							PGApplication.getApplication());
					mInstance = instance;
				}
			}
		}
		return mInstance;
	}

	@Override
	protected void initManager() {
		// TODO Auto-generated method stub
		getDBImageData();
	}

	@Override
	public void DestroyManager() {
		// TODO Auto-generated method stub
		DBTools.getInstance().closeDB();
	}

	public void addMainGroupImage(ImageResDataStruct data) {
		for (int i = 0; i < mainGroupImage.imageData.size(); i++) {
			if (mainGroupImage.imageData.get(i).res_id.equals(data.res_id))
				return;
		}
		mainGroupImage.imageData.add(data);
		DBTools.getInstance().insertImageData(data.res_id, data.parent_res_id,
				data.link, data.text, data.coin);
	}
	
	public void addMyGroupImage(ImageResDataStruct data) {
		for (int i = 0; i < myGroupImage.imageData.size(); i++) {
			if (myGroupImage.imageData.get(i).res_id.equals(data.res_id))
				return;
		}
		myGroupImage.imageData.add(data);
		DBTools.getInstance().updateBuyData(data.res_id);
	}

	public void addGroupImage(ImageResDataStruct data) {
		for (int m = 0; m < ImageDataManager.getInstance().groupImage.size(); m++) {
			GroupImageResDataStruct groupData = ImageDataManager.getInstance().groupImage
					.get(m);
			if (groupData.parent_res_id.equals(data.parent_res_id)) {
				for (int i = 0; i < groupData.imageData.size(); i++) {
					if (groupData.imageData.get(i).res_id.equals(data.res_id))
						return;
				}
				groupData.imageData.add(data);
				DBTools.getInstance().insertImageData(data.res_id,
						data.parent_res_id, data.link, data.text, data.coin);
			}
		}
	}

	private void getDBImageData() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getInstance().getAllMainImageData();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					ImageResDataStruct data = new ImageResDataStruct();
					data.res_id = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("res_id")));
					data.parent_res_id = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("parent_res_id")));
					data.link = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("link")));
					data.text = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("text")));
					data.coin = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("coin")));
					mainGroupImage.imageData.add(data);
					c.moveToNext();
				}
				c.close();
			}
		};
		thread.start();
		
		Thread threadMy = new Thread() {
			public void run() {
				Cursor c = DBTools.getInstance().getAllMyImageData();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					ImageResDataStruct data = new ImageResDataStruct();
					data.res_id = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("res_id")));
					data.parent_res_id = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("parent_res_id")));
					data.link = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("link")));
					data.text = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("text")));
					data.coin = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("coin")));
					myGroupImage.imageData.add(data);
					c.moveToNext();
				}
				c.close();
			}
		};
		threadMy.start();

//		Thread threadAll = new Thread() {
//			public void run() {
//				Cursor c = DBTools.getInstance().getAllImageData();
//				if (c == null)
//					return;
//				for (int i = 0; i < c.getCount(); i++) {
//					for (int m = 0; m < groupImage.size(); m++) {
//						String parent_res_id = DBTools.getUnvalidFormRs(c
//								.getString(c.getColumnIndex("parent_res_id")));
//						if (!parent_res_id
//								.equals(groupImage.get(m).parent_res_id))
//							continue;
//						ImageResDataStruct data = new ImageResDataStruct();
//						data.res_id = DBTools.getUnvalidFormRs(c.getString(c
//								.getColumnIndex("res_id")));
//						data.parent_res_id = DBTools.getUnvalidFormRs(c
//								.getString(c.getColumnIndex("parent_res_id")));
//						data.link = DBTools.getUnvalidFormRs(c.getString(c
//								.getColumnIndex("link")));
//						data.text = DBTools.getUnvalidFormRs(c.getString(c
//								.getColumnIndex("text")));
//						data.coin = DBTools.getUnvalidFormRs(c.getString(c
//								.getColumnIndex("coin")));
//						groupImage.get(m).imageData.add(data);
//						break;
//					}
//
//					GroupImageResDataStruct struct = new GroupImageResDataStruct();
//					groupImage.add(struct);
//					ImageResDataStruct data = new ImageResDataStruct();
//					data.res_id = DBTools.getUnvalidFormRs(c.getString(c
//							.getColumnIndex("res_id")));
//					data.parent_res_id = DBTools.getUnvalidFormRs(c.getString(c
//							.getColumnIndex("parent_res_id")));
//					data.link = DBTools.getUnvalidFormRs(c.getString(c
//							.getColumnIndex("link")));
//					data.text = DBTools.getUnvalidFormRs(c.getString(c
//							.getColumnIndex("text")));
//					data.coin = DBTools.getUnvalidFormRs(c.getString(c
//							.getColumnIndex("coin")));
//					struct.imageData.add(data);
//
//					c.moveToNext();
//				}
//				c.close();
//			}
//		};
//		threadAll.start();
	}

}
