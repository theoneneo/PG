package com.neo.prettygirl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.neo.prettygirl.controller.AppManager;
import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.data.ImageResDataStruct;
import com.neo.prettygirl.db.DBTools;

public class BuyDialogActivity extends BaseActivity implements
		UpdatePointsNotifier {
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

	private void initUI() {
		coin_text = (TextView) findViewById(R.id.coin_text);
		coin_text.setText(getString(R.string.total_coin)
				+ AppManager.getInstance().coin);

		LinearLayout layout = (LinearLayout) this
				.findViewById(R.id.AdLinearLayout);
		AppConnect.getInstance(this).showBannerAd(this, layout);

		Button wall_btn = (Button) findViewById(R.id.wall_btn);
		wall_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppConnect.getInstance(PGApplication.getContext()).showOffers(
						BuyDialogActivity.this);
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

	private void buyImageData() {
		AppConnect.getInstance(this).spendPoints(Integer.valueOf(curRes.coin),
				this);
		
		DBTools.getInstance().updateBuyData(res_id);
		
		Intent intent = new Intent(this, ImageDataActivity.class);
		intent.putExtra("res_id", res_id);
		startActivity(intent);
		finish();
	}

	private void IsBuyVailable() {
		if (Integer.valueOf(curRes.coin) > AppManager.getInstance().coin)
			buy_btn.setEnabled(false);
		else
			buy_btn.setEnabled(true);
	}

	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		// TODO Auto-generated method stub
		AppManager.getInstance().coin = arg1;
		coin_text.setText(getString(R.string.total_coin)
				+ AppManager.getInstance().coin);
		coin_text.invalidate();
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub

	}
}