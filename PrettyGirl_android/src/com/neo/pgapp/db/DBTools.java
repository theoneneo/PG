package com.neo.pgapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.neo.pgapp.PGApplication;
import com.neo.pgapp.db.DataBase.BUY_DATA_DB;

public class DBTools {
	private static DBTools mInstance;
	private static Context mContext;

	private DBTools(Context context) {
		mContext = context;
	}

	public static DBTools getInstance() {
		synchronized (DBTools.class) {
			if (mInstance == null) {
				mInstance = new DBTools(PGApplication.getContext());
			}
			return mInstance;
		}
	}

	public void closeDB() {
		DBContentProvider.closeDB();
	}

	public Cursor getAllMainImageData() {
		String selection = BUY_DATA_DB.PARENT_RES_ID + "='" + -1 + "'";
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

	public Cursor getAllMyImageData() {
		String selection = BUY_DATA_DB.PARENT_RES_ID + "='" + -1 + "'"
				+ " and " + BUY_DATA_DB.BUY + "='" + 1 + "'";
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

	public Cursor getAllResIdImageData(String parent_res_id) {
		String selection = BUY_DATA_DB.PARENT_RES_ID + "='" + parent_res_id
				+ "'";
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
	
	public Cursor getResIdImageData(String res_id) {
		String selection = BUY_DATA_DB.RES_ID + "='" + res_id
				+ "'";
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
	
//	public Cursor getAllImageData(){
//		String selection = BUY_DATA_DB.PARENT_RES_ID + "!='" + -1 + "'";
//		Cursor cursor = null;
//		try {
//			cursor = mContext.getContentResolver().query(
//					BUY_DATA_DB.CONTENT_URI, null, selection, null, null);
//			if (cursor != null) {
//				cursor.moveToFirst();
//				return cursor;
//			} else {
//				return null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return cursor;
//	}

//	public Cursor isBuyRes(String res_id) {
//		String selection = BUY_DATA_DB.RES_ID + "='" + res_id + "'" + " and "
//				+ BUY_DATA_DB.BUY + "='" + 1 + "'";
//		Cursor cursor = null;
//		try {
//			cursor = mContext.getContentResolver().query(
//					BUY_DATA_DB.CONTENT_URI, null, selection, null, null);
//			if (cursor != null) {
//				cursor.moveToFirst();
//				return cursor;
//			} else {
//				return null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return cursor;
//	}

	public void insertImageData(String res_id, String parent_res_id,
			String link, String text, String coin) {
		ContentValues value = new ContentValues();
		value.put("res_id", toValidRs(res_id));
		value.put("parent_res_id", toValidRs(parent_res_id));
		value.put("link", toValidRs(link));
		value.put("text", toValidRs(text));
		value.put("coin", toValidRs(coin));
		value.put("buy", 0);
		mContext.getContentResolver().insert(BUY_DATA_DB.CONTENT_URI, value);
	}

	public void updateBuyData(String res_id) {
		String selection = BUY_DATA_DB.RES_ID + "='" + toValidRs(res_id) + "'";
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
