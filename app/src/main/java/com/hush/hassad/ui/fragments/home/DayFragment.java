package com.hush.hassad.ui.fragments.home;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.dal.DAL;
import com.hush.hassad.ui.activities.MatchScreenActivity;
import com.hush.hassad.ui.adapters.MatchAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.competition.Match;

import java.util.ArrayList;
import java.util.Date;

public class DayFragment extends Fragment {

	private MatchAdapter matchAdapter;
    private ArrayList<Match> matches;
    private Date date;

    public DayFragment(){
    	matches = new ArrayList<>();
	}

	public View createView(LayoutInflater inflater, ViewGroup container){
		View view = inflater.inflate(R.layout.fragment_home_sub, container, false);
		ListView matchList = (ListView) view.findViewById(R.id.match_list);

		matchAdapter = new MatchAdapter(getActivity(), R.layout.card_match, matches ,null);
		matchList.setAdapter(matchAdapter);

		matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(),MatchScreenActivity.class);
				int pos = (int)matchAdapter.getItemId(position);
				Match match = (Match) matches.get(pos);
				intent.putExtra("match",match);
				startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return createView(inflater, container);
	}

	public void updateMatches(Date date){
		this.date = date;
		try{
			//matches = Manager.getInstance().getMatches(date);
			DAL.getInstance().updateMatches(this,date);
		}
		catch (Exception e){
			Toast.makeText(getActivity(), "Prob", Toast.LENGTH_SHORT).show();
		}
	}


	public void setDate(Date date){
    	this.date = date;
	}

	public void setMatches(ArrayList<Match> matches){
		this.matches.addAll(matches);
	}

	public void addMatch(Match m) {
		//TODO: insert sorted
		this.matches.add(m);
		if (matchAdapter != null) {
			matchAdapter.notifyDataSetChanged();
		}
	}
}
