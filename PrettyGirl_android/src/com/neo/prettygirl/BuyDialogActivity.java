package com.neo.prettygirl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

public class BuyDialogActivity extends BaseActivity {
	private String res_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
