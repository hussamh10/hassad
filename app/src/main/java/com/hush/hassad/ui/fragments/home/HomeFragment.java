package com.hush.hassad.ui.fragments.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hush.hassad.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

 	private ArrayList<DayFragment> days;

	public HomeFragment(){
		update();
	}

	public void update(){
		ArrayList<Date> dates = getDates(-2, 2);
		days = new ArrayList<>();
		for (Date d: dates) {
			DayFragment fragment = new DayFragment();
			fragment.updateMatches(d);
			days.add(fragment);
		}
	}

	//FIXME
	private ArrayList<Date> getDates(int min, int max){
		ArrayList<Date> dates = new ArrayList<>();

	    for (int i = min; i <= max; i++){
			dates.add(getDay(i));
		}
		return dates;
	}


	// FIXME move to utils
	private Date getDay(int i) {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, i);
		return cal.getTime();
	}

	// FIXME move to utils
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

	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container,false);

		setHasOptionsMenu(true);

		ViewPager viewPager = view.findViewById(R.id.viewpager);
		homePagerAdapter = new HomePagerAdapter(generateDates(), getChildFragmentManager());
		homePagerAdapter.days = days;
		viewPager.setAdapter(homePagerAdapter);
		viewPager.setCurrentItem(2);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.home_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
			case R.id.view_calendar:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	HomePagerAdapter homePagerAdapter;

	private static class HomePagerAdapter extends FragmentPagerAdapter {

		private ArrayList<DayFragment> days;
		private static DateFormat dateFormat = new SimpleDateFormat("E, d MMM", Locale.ENGLISH);

		HomePagerAdapter(Date[] dates, FragmentManager fm) {
			super(fm);
			this.dates = dates;
		}

		@Override
		public Fragment getItem(int position) {
			return days.get(position);
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