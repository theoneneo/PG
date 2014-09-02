package com.neo.prettygirl.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.neo.prettygirl.controller.AppManager;
import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.data.ImageResDataStruct;
import com.neo.prettygirl.tools.Utf8Code;

public class ProtocolDataInput {

	public static boolean parseMainImageListDataToJson(JSONObject obj)
			throws JSONException {
		if (obj == null) {
			return false;
		}
		try {
			JSONArray arrays = obj.getJSONArray("data");
			for (int i = 0; i < arrays.length(); i++) {
				JSONObject item = (JSONObject) arrays.opt(i);
				ImageResDataStruct data = new ImageResDataStruct();
				data.res_id = item.optString("res_id");
				data.link = item.optString("link");
				data.text = item.optString("text");
				data.coin = item.optString("coin");
				ImageDataManager.getInstance().addMainGroupImage(data);
			}
			return true;
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return false;
	}

	public static boolean parseAllImageListDataToJson(JSONObject obj)
			throws JSONException {
		if (obj == null) {
			return false;
		}
		try {
			JSONArray arrays = obj.getJSONArray("data");
			for (int i = 0; i < arrays.length(); i++) {
				JSONObject item = (JSONObject) arrays.opt(i);
				ImageResDataStruct data = new ImageResDataStruct();
				data.res_id = item.optString("res_id");
				data.parent_res_id = item.optString("parent_res_id");
				data.link = item.optString("link");
				data.text = Utf8Code.utf8Decode(item.optString("text"));
				data.coin = item.optString("coin");
				ImageDataManager.getInstance().addCurGroupImage(data);
			}
			return true;
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return false;
	}
	
	public static boolean parseUpdateApkToJson(JSONObject obj)
			throws JSONException {
		if (obj == null) {
			return false;
		}
		try {
			JSONArray arrays = obj.getJSONArray("data");
			for (int i = 0; i < arrays.length(); i++) {
				JSONObject item = (JSONObject) arrays.opt(i);
				int version = item.optInt("version");
				if(version > AppManager.curVersion)
					AppManager.updateLink = item.optString("link");			
				break;
			}
			return true;
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return false;
	}

}