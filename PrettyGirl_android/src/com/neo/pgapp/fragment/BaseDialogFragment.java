package com.neo.pgapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.neo.mtapp.R;

public abstract class BaseDialogFragment extends DialogFragment {

	private static String FLAG = "flag";
	protected Context mContext;

	public String getFlagStr() {
		return FLAG;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 閫忔槑
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
