package com.hush.hassad.ui.fragments.profile;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.MatchPrediction;
import com.hush.hassad.controller.predictions.Prediction;
import com.hush.hassad.ui.activities.MatchScreenActivity;
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
		ListView matchList = view.findViewById(R.id.predicted_matches_list);

		matchesPredictedAdapter = new MatchesPredictedAdapter(getActivity(), R.layout.card_match, predictedMatches ,null);
		matchList.setAdapter(matchesPredictedAdapter);

		return view;
	}

	public void setMatchesPredictedAdapter(ArrayList<MatchPrediction> predictedMatches){
		this.predictedMatches = predictedMatches;
	}

	public void update(User user){
		predictedMatches = Manager.getInstance().getPredictedMatches(user);
	}
}
