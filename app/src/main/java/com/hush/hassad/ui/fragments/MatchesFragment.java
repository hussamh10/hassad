package com.hush.hassad.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hush.hassad.R;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.predictions.MatchPrediction;
import com.hush.hassad.ui.adapters.MatchAdapter;
import com.hush.hassad.ui.adapters.MatchesPredictedAdapter;

import java.util.ArrayList;

public class MatchesFragment extends Fragment {

	MatchesPredictedAdapter matchesPredictedAdapter;
	private ArrayList<MatchPrediction> predictedMatches;

	public MatchesFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_matches_sub, container, false);
		ListView matchList = (ListView) view.findViewById(R.id.predicted_matches_list);

		matchesPredictedAdapter = new MatchesPredictedAdapter(getActivity(), R.layout.card_match, predictedMatches ,null);
		matchList.setAdapter(matchesPredictedAdapter);

		return view;
	}
}
