package com.neo.prettygirl.protocol;

import org.json.JSONException;
import org.json.JSONObject;

public class ProtocolDataOutput {
	public static JSONObject getMainImageListDataToJson(int num) throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("num", num);
			return output;
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject getAllImageListDataToJson(String res_id) throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("res_id", res_id);
			return output;
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject getUpdateApk(int versionCode) throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("version", versionCode);
			return output;
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
