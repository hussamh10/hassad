package com.hush.hassad.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hush.hassad.R;

public class ProfileFragment extends Fragment {
	private TabLayout tabLayout;
	ProfileViewpager profilePagerAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.fragment_profile, container,false);

		tabLayout = (TabLayout) view.findViewById(R.id.tabLayoutProfile);

		final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pagerProfile);
		profilePagerAdapter = new ProfileViewpager(getActivity().getFragmentManager(), getActivity());
		viewPager.setAdapter(profilePagerAdapter);

		tabLayout.setupWithViewPager(viewPager);
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}

		});

		return view;
	}


	public class ProfileViewpager extends FragmentPagerAdapter {

		final int PAGE_COUNT = 2;
		private String tabTitles[] = new String[]{"Matches","Groups","Tournament"};
		private Context context;

		public ProfileViewpager(FragmentManager fm, Context context) {
			super(fm);
			this.context = context;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position){
				case 0:
					return new MatchesFragment();
				case 1:
					return new GroupFragment();
				case 2:
					return new TournamentFragment();
				default:
					return null;
			}
		}

		@Override
		public int getCount() {
			return tabTitles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return tabTitles[position];
		}

	}
}
