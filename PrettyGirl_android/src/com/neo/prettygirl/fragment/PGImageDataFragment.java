package com.neo.prettygirl.fragment;

import me.maxwin.view.XListView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dodowaterfall.widget.ScaleImageView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.neo.prettygirl.ImageActivity;
import com.neo.prettygirl.ImageDataActivity;
import com.neo.prettygirl.R;
import com.neo.prettygirl.controller.AppManager;
import com.neo.prettygirl.controller.ImageDataManager;

public class PGImageDataFragment extends BaseFragment{
	private XListView mAdapterView = null;
	private ImageAdapter mAdapter = null;

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
		mAdapterView.setPullRefreshEnable(false);
		mAdapterView.setPullLoadEnable(false);
		mAdapter = new ImageAdapter(getActivity());
		mAdapterView.setAdapter(mAdapter);
		mAdapterView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				go2ImageActivity(position - 1);
			}
		});
	}

	public void updateAdapter(){
		mAdapter.notifyDataSetChanged();
	}
	
	private class ImageAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public ImageAdapter(Context context) {
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
				holder.row_text = (TextView) convertView
						.findViewById(R.id.row_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			AppManager.IMAGE_SD_CACHE.get(
					ImageDataManager.getInstance().curGroupImage.imageData
							.get(position).link, holder.row_image);
			holder.row_text.setVisibility(View.GONE);
//			holder.row_text
//					.setText(ImageDataManager.getInstance().curGroupImage.imageData
//							.get(position).res_id);

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ImageDataManager.getInstance().curGroupImage.imageData
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
		TextView row_text;
	}
	
	private void go2ImageActivity(int position){
		Intent intent = new Intent(getActivity(), ImageActivity.class);
		intent.putExtra("parent_res_id", ImageDataActivity.res_id);
		intent.putExtra("position", position);
		startActivity(intent);
	}
}
