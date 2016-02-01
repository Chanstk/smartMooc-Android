package com.example.lenovo.viewPager;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class fragmentPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragList;
	private List<String> titleList;
	

	public fragmentPagerAdapter(FragmentManager fm, List<Fragment> fragList,
			List<String> titleList) {
		super(fm);
		this.fragList = fragList;
		this.titleList = titleList;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragList.size();
	}
    @Override
    public CharSequence getPageTitle(int position) {
    	// TODO Auto-generated method stub
    	return titleList.get(position);
    }
}
