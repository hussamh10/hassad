package com.hush.hassad.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.ui.adapters.MatchAdapter;

import java.util.ArrayList;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.competition.Match;

import java.util.ArrayList;
import java.util.Date;

public class DayFragment extends Fragment {

	MatchAdapter matchAdapter;
    ArrayList<Match> matches;

    public DayFragment(){
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home_sub, container, false);

		// FIXME shouldnt the matches be populd

		Bundle bundle = this.getArguments();

		Date date = new Date();
		if (bundle != null) {
			date = (Date) bundle.getSerializable("date");
		}

		try{
			matches = Manager.getInstance().getMatches(date);
		}
		catch (Exception e){
			Toast.makeText(getActivity(), "Problem with get matches", Toast.LENGTH_SHORT).show();
		}

		ListView matchList = (ListView) view.findViewById(R.id.match_list);


		matchAdapter = new MatchAdapter(getActivity(), R.layout.card_match, matches ,null);
		matchList.setAdapter(matchAdapter);


		return view;
	}
}
