package com.hush.hassad.ui.fragments.profile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.player.User;

public class ProfileFragment extends Fragment {
	private TabLayout tabLayout;
	ProfileViewpager profilePagerAdapter;
	MatchesFragment predicted_matches;
	GroupFragment predicted_groups;
	TournamentFragment predicted_tournament;

	public ProfileFragment(){
	}

	public void update(User user){
		predicted_matches = new MatchesFragment();
		predicted_matches.update(user);

		predicted_groups = new GroupFragment();
		predicted_groups.update(user);

		predicted_tournament = new TournamentFragment();
		predicted_tournament.update(user);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.fragment_profile, container,false);

		tabLayout = (TabLayout) view.findViewById(R.id.tabLayoutProfile);

		final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pagerProfile);
		profilePagerAdapter = new ProfileViewpager(getChildFragmentManager(), getActivity());
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
					return predicted_matches;
				case 1:
					return predicted_groups;
				case 2:
					return predicted_tournament;
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
