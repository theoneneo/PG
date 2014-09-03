package com.neo.pgapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {   
    public static final class BUY_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.neo.pgapp.db.provider/buy_data");
	public static final String _ID = "_id";
	public static final String RES_ID = "res_id";
	public static final String PARENT_RES_ID = "parent_res_id";
	public static final String LINK = "link";
	public static final String TEXT = "text";
	public static final String COIN = "coin";
	public static final String BUY = "buy";

	public static final String CREATE_TABLE = "CREATE TABLE buy_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", res_id TEXT, parent_res_id TEXT, link TEXT, text TEXT, coin TEXT, buy INTEGER);";
    }
}
