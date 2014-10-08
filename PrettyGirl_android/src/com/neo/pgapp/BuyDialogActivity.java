package com.neo.pgapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.neo.mtapp.R;
import com.neo.pgapp.controller.AppManager;
import com.neo.pgapp.controller.ImageDataManager;
import com.neo.pgapp.data.ImageResDataStruct;

public class BuyDialogActivity extends BaseActivity{
	private String res_id;
	private ImageResDataStruct curRes;
	private TextView coin_text;
	private Button wall_btn, buy_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		initData();
		setContentView(R.layout.activity_buy);
		initUI();
	}

	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		IsBuyVailable();
		coin_text.setText(getString(R.string.total_coin)
				+ AppManager.getInstance().coin);
		coin_text.invalidate();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void onWindowFocusChanged (boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		IsBuyVailable();
		coin_text.setText(getString(R.string.total_coin)
				+ AppManager.getInstance().coin);
		coin_text.invalidate();
	}

	private void initUI() {
		coin_text = (TextView) findViewById(R.id.coin_text);
		coin_text.setText(getString(R.string.total_coin)
				+ AppManager.getInstance().coin);


		Button wall_btn = (Button) findViewById(R.id.wall_btn);
		wall_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showOffers();
			}
		});

		buy_btn = (Button) findViewById(R.id.buy_btn);
		buy_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				buyImageData();
			}
		});
		buy_btn.setText(getString(R.string.spend) + curRes.coin);

		IsBuyVailable();
	}

	private void initData() {
		res_id = getIntent().getExtras().getString("res_id");
		for (int i = 0; i < ImageDataManager.getInstance().mainGroupImage.imageData
				.size(); i++) {
			ImageResDataStruct data = ImageDataManager.getInstance().mainGroupImage.imageData
					.get(i);
			if (data.res_id.equals(res_id)) {
				curRes = data;
				break;
			}
		}
	}

	private void showOffers() {

	}

	private void buyImageData() {

		ImageDataManager.getInstance().addMyGroupImage(curRes);

		Intent intent = new Intent(this, ImageDataActivity.class);
		intent.putExtra("res_id", curRes.res_id);
		startActivity(intent);
		finish();
	}

	private void IsBuyVailable() {
		if (Integer.valueOf(curRes.coin) > AppManager.getInstance().coin)
			buy_btn.setEnabled(false);
		else
			buy_btn.setEnabled(true);
	}
}
