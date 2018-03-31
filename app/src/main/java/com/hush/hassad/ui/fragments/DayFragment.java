package com.hush.hassad.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hush.hassad.R;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.ui.adapters.MatchAdapter;

import java.util.ArrayList;

public class DayFragment extends Fragment {

	ArrayList<Match> matches;
	MatchAdapter matchAdapter;

	public DayFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home_sub, container, false);
		ListView matchList = (ListView) view.findViewById(R.id.match_list);
		matchAdapter = new MatchAdapter(getActivity(), R.layout.card_match, matches ,null);
		matchList.setAdapter(matchAdapter);
		return view;
	}
}
