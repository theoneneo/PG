package com.neo.pgapp.view;

import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.neo.pgapp.BuyDialogActivity;
import com.neo.mtapp.R;
import com.neo.pgapp.controller.AppManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AdLinearLayoutView extends LinearLayout implements
		UpdatePointsNotifier {
	private Context context;

	public AdLinearLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (isNetworkConnected(context)) {
				AppConnect.getInstance(context).awardPoints(50, this);
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		// TODO Auto-generated method stub
		AppManager.getInstance().coin = arg1;
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub
	}

	private boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
