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

	private MatchAdapter matchAdapter;
    private ArrayList<Match> matches;
    private Date date;

    public DayFragment(){ }

	public View createView(LayoutInflater inflater, ViewGroup container){
		View view = inflater.inflate(R.layout.fragment_home_sub, container, false);
		ListView matchList = (ListView) view.findViewById(R.id.match_list);

		matchAdapter = new MatchAdapter(getActivity(), R.layout.card_match, matches ,null);
		matchList.setAdapter(matchAdapter);

		return view;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return createView(inflater, container);
	}

	public void updateMatches(Date date){
		this.date = date;
		try{
			matches = Manager.getInstance().getMatches(date);
		}
		catch (Exception e){
			Toast.makeText(getActivity(), "Prob", Toast.LENGTH_SHORT).show();
		}
	}

	public void updateMatches(){
		try{
			matches = Manager.getInstance().getMatches(date);
		}
		catch (Exception e){
			Toast.makeText(getActivity(), "Prob", Toast.LENGTH_SHORT).show();
		}
	}

	public void setDate(Date date){
    	this.date = date;
	}
}
