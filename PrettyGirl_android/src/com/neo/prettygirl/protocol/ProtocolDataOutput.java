package com.neo.prettygirl.protocol;

import org.json.JSONException;
import org.json.JSONObject;

public class ProtocolDataOutput {
	public static JSONObject getMainImageListDataToJson(int page) throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("page", page);
			return output;
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject getAllImageListDataToJson(String res_id, int page) throws JSONException {
		try {
			JSONObject output = new JSONObject();
			output.put("res_id", res_id);
			output.put("page", page);
			return output;
		} catch (JSONException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
