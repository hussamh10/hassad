package com.hush.hassad;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

public class HomeFragment extends Fragment {
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container,false);
		ViewPager viewPager = view.findViewById(R.id.viewpager);
		homePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
		viewPager.setAdapter(homePagerAdapter);
		viewPager.setCurrentItem(3);
		return view;
	}

	HomePagerAdapter homePagerAdapter;

	private static class HomePagerAdapter extends FragmentPagerAdapter {

		HomePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new HomeSubFragment();
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "hello";
		}

	}

}
