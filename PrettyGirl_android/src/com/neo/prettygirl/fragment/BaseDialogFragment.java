package com.neo.prettygirl.fragment;

import com.neo.prettygirl.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public abstract class BaseDialogFragment extends DialogFragment {

	private static String FLAG = "flag";
	protected Context mContext;

	/**
	 * 获取Fragment的标记字符串,每个Fragment都是唯一的�? 暂时不用�?
	 */
	public String getFlagStr() {
		return FLAG;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 透明
		setStyle(DialogFragment.STYLE_NORMAL,
				R.style.dialogfragment_transparent_bg);
		mContext = getActivity().getApplicationContext();

		Bundle args = getArguments();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
