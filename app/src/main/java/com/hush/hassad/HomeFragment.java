package com.hush.hassad;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hush.hassad.view.home.DayFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container,false);

		setHasOptionsMenu(true);

		ViewPager viewPager = view.findViewById(R.id.viewpager);
		homePagerAdapter = new HomePagerAdapter(generateDates(), getChildFragmentManager());
		viewPager.setAdapter(homePagerAdapter);
		viewPager.setCurrentItem(2);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.home_menu, menu);
	}

	private Date[] generateDates() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -2);

		Date[] dates = new Date[5];

		for (int i = 0; i < 5; ++i) {
			dates[i] = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		return dates;
	}

	HomePagerAdapter homePagerAdapter;

	private static class HomePagerAdapter extends FragmentPagerAdapter {

		private static DateFormat dateFormat = new SimpleDateFormat("E, d MMM", Locale.ENGLISH);

		HomePagerAdapter(Date[] dates, FragmentManager fm) {
			super(fm);
			this.dates = dates;
		}

		@Override
		public Fragment getItem(int position) {
			return new DayFragment();
		}

		@Override
		public int getCount() {
			return dates.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return dateFormat.format(dates[position]);
		}

		private Date[] dates;
	}
}
