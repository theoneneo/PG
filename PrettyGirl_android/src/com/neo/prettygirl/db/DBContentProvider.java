package com.neo.prettygirl.db;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.neo.prettygirl.db.DataBase.BUY_DATA_DB;

public class DBContentProvider extends ContentProvider {
	private static DBProviderHelper dbHelper;

	private static final String URI_AUTHORITY = "com.neo.prettygirl.db.provider";
	private static final String DATABASE_NAME = "prettygirl_data.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TB_BUY_DATA = "buy_data";


	private static final int BUY_DATA = 1;
	private static final int BUY_DATA_ID = 2;

	private static HashMap<String, String> buyDataMap;

	private static final UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		sUriMatcher.addURI(URI_AUTHORITY, "buy_data", BUY_DATA);
		sUriMatcher.addURI(URI_AUTHORITY, "buy_data/#", BUY_DATA_ID);


		buyDataMap = new HashMap<String, String>();
		buyDataMap.put(BUY_DATA_DB._ID, BUY_DATA_DB._ID);
		buyDataMap.put(BUY_DATA_DB.RES_ID, BUY_DATA_DB.RES_ID);
		buyDataMap.put(BUY_DATA_DB.PARENT_RES_ID, BUY_DATA_DB.PARENT_RES_ID);
		buyDataMap.put(BUY_DATA_DB.LINK, BUY_DATA_DB.LINK);
		buyDataMap.put(BUY_DATA_DB.TEXT, BUY_DATA_DB.TEXT);
		buyDataMap.put(BUY_DATA_DB.COIN, BUY_DATA_DB.COIN);
		buyDataMap.put(BUY_DATA_DB.BUY, BUY_DATA_DB.BUY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		dbHelper = new DBProviderHelper(getContext(), DATABASE_NAME, null,
				DATABASE_VERSION);
		return true;
	}

	public static void closeDB() {
		dbHelper.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case BUY_DATA:
			qb.setTables(TB_BUY_DATA);
			qb.setProjectionMap(buyDataMap);
			break;
		case BUY_DATA_ID:
			qb.setTables(TB_BUY_DATA);
			qb.setProjectionMap(buyDataMap);
			qb.appendWhere(BUY_DATA_DB._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initValues) {
		ContentValues values = null;
		if (initValues == null) {
			return null;
		} else {
			values = new ContentValues(initValues);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Uri myURI;
		long retval = 0;
		switch (sUriMatcher.match(uri)) {
		case BUY_DATA:
			retval = db.insert(TB_BUY_DATA, BUY_DATA_DB._ID, values);
			myURI = BUY_DATA_DB.CONTENT_URI;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (retval > 0) {
			Uri retUri = ContentUris.withAppendedId(myURI, retval);
			retUri.buildUpon().build();
			getContext().getContentResolver().notifyChange(retUri, null);

			return retUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int retval = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (sUriMatcher.match(uri)) {
		case BUY_DATA:
			retval = db.update(TB_BUY_DATA, values, selection, selectionArgs);
			break;
		case BUY_DATA_ID:
			retval = db.update(TB_BUY_DATA, values,
					BUY_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			break;

		}

		getContext().getContentResolver().notifyChange(uri, null);
		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int retval = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (sUriMatcher.match(uri)) {
		case BUY_DATA:
			retval = db.delete(TB_BUY_DATA, selection, selectionArgs);
			break;
		case BUY_DATA_ID:
			retval = db.delete(TB_BUY_DATA,
					BUY_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			break;
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return retval;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLiteDatabase getHelper() {
		return dbHelper.getWritableDatabase();
	}

}

/**
 * @author jeff
 * 
 */
class DBProviderHelper extends SQLiteOpenHelper {
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	private static final String TB_BUY_DATA = "buy_data";

	public DBProviderHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BUY_DATA_DB.CREATE_TABLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_BUY_DATA);
		onCreate(db);
	}
}
