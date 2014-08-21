package com.neo.prettygirl.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.data.GroupImageResDataStruct;
import com.neo.prettygirl.data.ImageResDataStruct;

import android.text.TextUtils;

public class ProtocolDataInput {

	public static boolean parseMainImageListDataToJson(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return false;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject obj = (JSONObject) jsonParser.nextValue();
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

	public static boolean parseAllImageListDataToJson(String input)
			throws JSONException {
		if (input == null || TextUtils.isEmpty(input)) {
			return false;
		}
		try {
			JSONTokener jsonParser = new JSONTokener(input);
			JSONObject obj = (JSONObject) jsonParser.nextValue();
			JSONArray arrays = obj.getJSONArray("data");
			for (int i = 0; i < arrays.length(); i++) {
				JSONObject item = (JSONObject) arrays.opt(i);
				ImageResDataStruct data = new ImageResDataStruct();
				data.res_id = item.optString("res_id");
				data.parent_res_id = item.optString("parent_res_id");
				data.link = item.optString("link");
				data.text = item.optString("text");
				data.coin = item.optString("coin");
				ImageDataManager.getInstance().addGroupImage(data);
			}
			return true;
		} catch (JSONException ex) {
			// 异常处理代码
		} catch (Exception e) {

		}
		return false;
	}

}