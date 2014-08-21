package com.neo.prettygirl;

import com.neo.prettygirl.event.BroadCastEvent;
import com.neo.prettygirl.fragment.PGMainFragment;

import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


public class MainActivity extends FragmentActivity {
	private static final String[] CONTENT = new String[] { "百度", "我的"};

	private MainAdapter adapter;
	private PGMainFragment mainListFragment;
	private PGMainFragment messageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, BroadCastEvent.class);
        setContentView(R.layout.activity_main);
        initUI();
    }
    
    protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
    
	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.GET_MAIN_IMAGE_LIST_DATA:
			if ((Boolean) event.getObject()) {
			}
			break;		
		case BroadCastEvent.GET_ALL_IMAGE_LIST_DATA:
			if ((Boolean) event.getObject()) {
			}
			break;
		default:
			break;
		}
	}
    
    private void initUI(){
		adapter = new MainAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
    }
    
	class MainAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
		public MainAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				if (mainListFragment == null)
					mainListFragment = new PGMainFragment();
				return mainListFragment;
			} else if (position == 1) {
				if (messageListFragment == null)
					messageListFragment = new PGMainFragment();
				return messageListFragment;
			}
			return mainListFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getIconResId(int index) {
			return 0;
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

}
