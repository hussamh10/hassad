package com.hush.hassad.ui.fragments.profile;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.TournamentPrediction;

public class TournamentFragment extends Fragment {

	TournamentPrediction prediction;

	public TournamentFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tournament_sub, container, false);

		// Create adapter and stuff
	}

	public void update(User user){
		prediction = Manager.getInstance().getTournamentPrediction(user);
	}
}
