package com.neo.prettygirl;

import com.neo.prettygirl.fragment.PGMainFragment;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
	private static final String[] CONTENT = new String[] { "百度", "我的"};

	private MainAdapter adapter;
	private PGMainFragment notificListFragment;
	private PGMainFragment messageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
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
				if (notificListFragment == null)
					notificListFragment = new PGMainFragment();
				return notificListFragment;
			} else if (position == 1) {
				if (messageListFragment == null)
					messageListFragment = new PGMainFragment();
				return messageListFragment;
			}
			return notificListFragment;
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
