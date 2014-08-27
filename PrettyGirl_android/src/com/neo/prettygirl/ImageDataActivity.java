package com.neo.prettygirl;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.waps.AppConnect;

import com.neo.prettygirl.controller.ImageDataManager;
import com.neo.prettygirl.controller.NetServiceManager;
import com.neo.prettygirl.data.ImageResDataStruct;
import com.neo.prettygirl.event.BroadCastEvent;
import com.neo.prettygirl.fragment.PGImageDataFragment;
import com.neo.prettygirl.waps.SlideWall;

import de.greenrobot.event.EventBus;

public class ImageDataActivity extends BaseActivity {
	public static String res_id;
	private ImageResDataStruct curRes;
	private PGImageDataFragment imageDataFragment;
	private View slidingDrawerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		EventBus.getDefault().register(this, BroadCastEvent.class);
		setContentView(R.layout.activity_data);
		initUI();
		
		AppConnect.getInstance(this).showPopAd(this);
	}

	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		if(SlideWall.getInstance().slideWallDrawer != null
				&& SlideWall.getInstance().slideWallDrawer.isOpened()){				
			// 如果抽屉式应用墙展示中，则关闭抽屉
			SlideWall.getInstance().closeSlidingDrawer();
			return;
		}
		super.onBackPressed();
	}

	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.GET_ALL_IMAGE_LIST_DATA:
			imageDataFragment.updateAdapter();
			break;
		default:
			break;
		}
	}

	private void initUI() {
		imageDataFragment = (PGImageDataFragment) getSupportFragmentManager()
				.findFragmentById(R.id.list);

		ImageButton leftBtn = (ImageButton) findViewById(R.id.title)
				.findViewById(R.id.left_btn);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView titleText = (TextView) findViewById(R.id.title).findViewById(
				R.id.title_text);
		titleText.setText(curRes.text);
		
    	slidingDrawerView = SlideWall.getInstance().getView(this);
    	if(slidingDrawerView != null){
    		addContentView(slidingDrawerView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	}
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
		ImageDataManager.getInstance().getDBResIdImageData(res_id);
		NetServiceManager.getInstance().getResImageListData(res_id);// 第一页
	}
}
