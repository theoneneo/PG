package com.neo.prettygirl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.controller.NetServiceManager;
import com.neo.prettygirl.data.ImageResDataStruct;
import com.neo.prettygirl.event.BroadCastEvent;
import com.neo.prettygirl.fragment.PGImageDataFragment;

import de.greenrobot.event.EventBus;

public class ImageActivity extends FragmentActivity {
	private String res_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		initData();
		setContentView(R.layout.activity_data);
		initUI();
	}

	protected void onDestroy() {
		super.onDestroy();
	}


	private void initUI() {

	}

	private void initData() {
		res_id = getIntent().getExtras().getString("res_id");
	}
}
