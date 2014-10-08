package com.neo.pgapp.fragment;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dodowaterfall.widget.ScaleImageView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.neo.mtapp.R;
import com.neo.pgapp.BuyDialogActivity;
import com.neo.pgapp.ImageDataActivity;
import com.neo.pgapp.controller.AppManager;
import com.neo.pgapp.controller.ImageDataManager;
import com.neo.pgapp.controller.NetServiceManager;
import com.neo.pgapp.data.ImageResDataStruct;
import com.neo.pgapp.db.DBTools;

public class PGMainFragment extends BaseFragment implements IXListViewListener {
	private XListView mAdapterView = null;
	private MainImageAdapter mAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_multilist, null);
		initView(v);
		return v;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void initView(View v) {
		mAdapterView = (XListView) v.findViewById(R.id.list);
		mAdapterView.setPullRefreshEnable(true);
		mAdapterView.setPullLoadEnable(false);
		mAdapterView.setXListViewListener(this);
		mAdapter = new MainImageAdapter(getActivity());
		mAdapterView.setAdapter(mAdapter);
		mAdapterView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				buyImageData(position);
			}
		});
	}

	private void buyImageData(int position) {
		String res_id = ImageDataManager.getInstance().mainGroupImage.imageData
				.get(position - 1).res_id;
		int buy = 0;
		Cursor c = DBTools.getInstance().getResIdImageData(res_id);
		if (c == null)
			return;
		for (int i = 0; i < c.getCount(); i++) {
			ImageResDataStruct data = new ImageResDataStruct();
			data.res_id = DBTools.getUnvalidFormRs(c.getString(c
					.getColumnIndex("res_id")));
			data.parent_res_id = DBTools.getUnvalidFormRs(c.getString(c
					.getColumnIndex("parent_res_id")));
			data.link = DBTools.getUnvalidFormRs(c.getString(c
					.getColumnIndex("link")));
			data.text = DBTools.getUnvalidFormRs(c.getString(c
					.getColumnIndex("text")));
			data.coin = DBTools.getUnvalidFormRs(c.getString(c
					.getColumnIndex("coin")));

			buy = c.getInt(c.getColumnIndex("buy"));
			if (buy == 1) {
				c.close();
				DBTools.getInstance().closeDB();
				go2ImageDataActivity(res_id);
			} else {
				String coin = null;
				coin = DBTools.getUnvalidFormRs(c.getString(c
						.getColumnIndex("coin")));
				if (coin != null) {
					if (!coin.equals("0")) {
						if(AppManager.isOpen)
							popBuyWindow(res_id);
						else
							go2ImageDataActivity(res_id);
					} else {
						ImageDataManager.getInstance().addMyGroupImage(data);
						go2ImageDataActivity(res_id);
					}
				} else {
					Toast.makeText(getActivity(), "未找到", Toast.LENGTH_SHORT)
							.show();
				}
				c.close();
				DBTools.getInstance().closeDB();
			}
			return;
		}
	}

	public void updateMainAdapter() {
		mAdapter.notifyDataSetChanged();
		mAdapterView.stopRefresh();
	}

	private class MainImageAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public MainImageAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = (View) inflater.inflate(R.layout.item_grid,
						parent, false);
				holder = new ViewHolder();
				holder.row_image = (ScaleImageView) convertView
						.findViewById(R.id.row_image);
				holder.row_coin = (ImageView) convertView
						.findViewById(R.id.row_coin);
				holder.row_text = (TextView) convertView
						.findViewById(R.id.row_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			
			ImageResDataStruct data = ImageDataManager.getInstance().mainGroupImage.imageData
					.get(position);

			holder.row_image.setTag(data.link);
			
			if(!AppManager.IMAGE_SD_CACHE.get(data.link, holder.row_image)){
				holder.row_image.setImageResource(R.drawable.empty_photo);
			}

			holder.row_text.setVisibility(View.GONE);
			holder.row_coin.setVisibility(View.GONE);

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ImageDataManager.getInstance().mainGroupImage.imageData
					.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	private class ViewHolder {
		ScaleImageView row_image;
		ImageView row_coin;
		TextView row_text;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		NetServiceManager.getInstance().getMainImageListData(
				ImageDataManager.getInstance().mainGroupImage.imageData.size(), AppManager.APP_PID);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	private void go2ImageDataActivity(String res_id) {
		Intent intent = new Intent(getActivity(), ImageDataActivity.class);
		intent.putExtra("res_id", res_id);
		startActivity(intent);
	}

	private void popBuyWindow(String res_id) {
		Intent intent = new Intent(getActivity(), BuyDialogActivity.class);
		intent.putExtra("res_id", res_id);
		startActivity(intent);
	}
}
