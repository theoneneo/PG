package com.neo.pgapp.controller;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.neo.pgapp.PGApplication;
import com.neo.pgapp.event.BroadCastEvent;
import com.neo.pgapp.protocol.ProtocolDataInput;
import com.neo.pgapp.protocol.ProtocolDataOutput;

import de.greenrobot.event.EventBus;

public class NetServiceManager extends BaseManager {
	private static NetServiceManager mInstance;

	private static final String NET_SERVER = "http://infocomm.duapp.com/";
	private static final String NET_GET_MAIN_IMAGE_LIST = NET_SERVER
			+ "getmainimage.py";
	private static final String NET_GET_TEST_IMAGE_LIST = NET_SERVER
			+ "gettestimage.py";
	private static final String NET_GET_ALL_IMAGE_LIST = NET_SERVER + "getallimage.py";
	private static final String NET_GET_UPDATE_APK = NET_SERVER + "getupdate.py";

	private NetServiceManager(Application app) {
		super(app);
		// TODO Auto-generated constructor stub
		initManager();
	}

	public static NetServiceManager getInstance() {
		NetServiceManager instance;
		if (mInstance == null) {
			synchronized (NetServiceManager.class) {
				if (mInstance == null) {
					instance = new NetServiceManager(
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

	}

	@Override
	public void DestroyManager() {
		// TODO Auto-generated method stub

	}

	public void getMainImageListData(int num, String APP_PID) {
		RequestQueue mQueue = Volley
				.newRequestQueue(PGApplication.getContext());
		try {
			JSONObject obj = ProtocolDataOutput
					.getMainImageListDataToJson(num, APP_PID);
			String url = null;
			if(AppManager.isOpen)
				url = NET_GET_MAIN_IMAGE_LIST;
			else
				url = NET_GET_TEST_IMAGE_LIST;
			mQueue.add(new JsonObjectRequest(Method.POST,
					url, obj, new Listener() {

						@Override
						public void onResponse(Object arg0) {
							// TODO Auto-generated method stub
							try {
								boolean isSuccess = ProtocolDataInput
										.parseMainImageListDataToJson((JSONObject)arg0);
								EventBus.getDefault()
										.post(new BroadCastEvent(
												BroadCastEvent.GET_MAIN_IMAGE_LIST_DATA,
												isSuccess));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							EventBus.getDefault()
									.post(new BroadCastEvent(
											BroadCastEvent.GET_MAIN_IMAGE_LIST_DATA,
											false));
						}
					}));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mQueue.start();
	}

	public void getResImageListData(String res_id) {
		RequestQueue mQueue = Volley
				.newRequestQueue(PGApplication.getContext());
		try {
			JSONObject obj = ProtocolDataOutput.getAllImageListDataToJson(
					res_id);
			mQueue.add(new JsonObjectRequest(Method.POST,
					NET_GET_ALL_IMAGE_LIST, obj, new Listener() {

						@Override
						public void onResponse(Object arg0) {
							// TODO Auto-generated method stub
							try {
								boolean isSuccess = ProtocolDataInput
										.parseAllImageListDataToJson((JSONObject)arg0);
								EventBus.getDefault()
										.post(new BroadCastEvent(
												BroadCastEvent.GET_ALL_IMAGE_LIST_DATA,
												isSuccess));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							EventBus.getDefault()
									.post(new BroadCastEvent(
											BroadCastEvent.GET_ALL_IMAGE_LIST_DATA,
											false));
						}
					}));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mQueue.start();
	}
	
	public void getUpdateApk(int versionCode, String APP_PID) {
		RequestQueue mQueue = Volley
				.newRequestQueue(PGApplication.getContext());
		try {
			JSONObject obj = ProtocolDataOutput.getUpdateApk(
					versionCode, APP_PID);
			mQueue.add(new JsonObjectRequest(Method.POST,
					NET_GET_UPDATE_APK, obj, new Listener() {

						@Override
						public void onResponse(Object arg0) {
							// TODO Auto-generated method stub
							try {
								boolean isSuccess = ProtocolDataInput
										.parseUpdateApkToJson((JSONObject)arg0);
								EventBus.getDefault()
										.post(new BroadCastEvent(
												BroadCastEvent.GET_UPDATE_APK,
												isSuccess));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							EventBus.getDefault()
									.post(new BroadCastEvent(
											BroadCastEvent.GET_UPDATE_APK,
											false));
						}
					}));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mQueue.start();
	}
}
