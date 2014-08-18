package com.neo.prettygirl.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {   
    public static final class BUY_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.neo.prettygirl.db.provider/buy_data");
	public static final String _ID = "_id";
	public static final String DATA_ID = "data_id";
	public static final String BUY = "buy";

	public static final String CREATE_TABLE = "CREATE TABLE buy_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", data_id TEXT, buy INTEGER);";
    }
}
