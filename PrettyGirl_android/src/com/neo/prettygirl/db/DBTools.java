package com.neo.prettygirl.db;

import com.neo.prettygirl.db.DataBase.BUY_DATA_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBTools {
	private static DBTools mInstance;
	private static Context mContext;

	private DBTools(Context context) {
		mContext = context;
	}

	public static DBTools instance(Context context) {
		synchronized (DBTools.class) {
			if (mInstance == null) {
				mInstance = new DBTools(context);
			}
			return mInstance;
		}
	}

	public static DBTools instance() {
		return mInstance;
	}

	public void closeDB() {
		DBContentProvider.closeDB();
	}
	
	public static Cursor getAllData() {
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					BUY_DATA_DB.CONTENT_URI, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}
	
	public static Cursor getAllBuyData() {
		String selection = BUY_DATA_DB.BUY + "='" + 1 + "'";
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					BUY_DATA_DB.CONTENT_URI, null, selection, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}
	
	public void insertBuyData(String id) {
		ContentValues value = new ContentValues();
		value.put("data_id", toValidRs(id));
		value.put("buy", 0);
		mContext.getContentResolver().insert(BUY_DATA_DB.CONTENT_URI, value);
	}
	
	public void updateBuyData(String id){
		String selection = BUY_DATA_DB.DATA_ID + "='" + toValidRs(id) + "'";
		ContentValues value = new ContentValues();
		value.put("buy", 1);
		mContext.getContentResolver().update(BUY_DATA_DB.CONTENT_URI, value,
				selection, null);		
	}
	
	public static String toValidRs(String obj) {
		if (obj == null)
			return "@*@";
		else if (obj.indexOf("'") != -1) {
			return obj.replace("'", "*@*");
		} else
			return obj;
	}

	public static String getUnvalidFormRs(String obj) {
		if (obj == null)
			return null;
		else if (obj.equals("@*@"))
			return null;
		else if (obj.indexOf("*@*") != -1) {
			return obj.replace("*@*", "'");
		} else
			return obj;
	}
}
