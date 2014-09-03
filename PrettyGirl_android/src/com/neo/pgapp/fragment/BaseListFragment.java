package com.neo.pgapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public abstract class BaseListFragment extends ListFragment {

	static final String FLAG = "flag";
	protected Context mContext;

	/**
	 * 获取Fragment的标记字符串,每个Fragment都是唯一的�? 暂时不用�?
	 */
	public String getFlagStr() {
		return "";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
