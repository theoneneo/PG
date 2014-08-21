package com.neo.prettygirl.controller;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.neo.prettygirl.PGApplication;
import com.neo.prettygirl.event.BroadCastEvent;
import com.neo.prettygirl.protocol.ProtocolDataInput;
import com.neo.prettygirl.protocol.ProtocolDataOutput;

import de.greenrobot.event.EventBus;

public class NetServiceManager extends BaseManager {
	private static NetServiceManager mInstance;

	private static final String NET_GET_MAIN_IMAGE_LIST = "";
	private static final String NET_GET_ALL_IMAGE_LIST = "";

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

	public void getMainImageListData(int page) {
		RequestQueue mQueue = Volley
				.newRequestQueue(PGApplication.getContext());
		try {
			JSONObject obj = ProtocolDataOutput
					.getMainImageListDataToJson(page);
			mQueue.add(new JsonObjectRequest(Method.POST, NET_GET_MAIN_IMAGE_LIST,
					obj, new Listener() {

						@Override
						public void onResponse(Object arg0) {
							// TODO Auto-generated method stub
							try {
								EventBus.getDefault()
										.post(new BroadCastEvent(
												BroadCastEvent.GET_MAIN_IMAGE_LIST_DATA,
												ProtocolDataInput
														.parseMainImageListDataToJson((String) arg0)));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}, new ErrorListener(){
						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							EventBus.getDefault()
							.post(new BroadCastEvent(
									BroadCastEvent.GET_MAIN_IMAGE_LIST_DATA, false));
						}			
					}));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mQueue.start();
	}
	
	public void getAllImageListData(String res_id, int page) {
		RequestQueue mQueue = Volley
				.newRequestQueue(PGApplication.getContext());
		try {
			JSONObject obj = ProtocolDataOutput
					.getAllImageListDataToJson(res_id, page);
			mQueue.add(new JsonObjectRequest(Method.POST, NET_GET_ALL_IMAGE_LIST,
					obj, new Listener() {

						@Override
						public void onResponse(Object arg0) {
							// TODO Auto-generated method stub
							try {
								EventBus.getDefault()
										.post(new BroadCastEvent(
												BroadCastEvent.GET_ALL_IMAGE_LIST_DATA,
												ProtocolDataInput
														.parseAllImageListDataToJson((String) arg0)));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}, new ErrorListener(){
						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							EventBus.getDefault()
							.post(new BroadCastEvent(
									BroadCastEvent.GET_ALL_IMAGE_LIST_DATA, false));
						}			
					}));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mQueue.start();
	}

}
