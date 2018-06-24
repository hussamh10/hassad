package com.hush.hassad.ui.fragments.home;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DayFragment extends Fragment {

	private MatchAdapter matchAdapter;
    private ArrayList<Match> matches;
    private Date date;
	ListView matchList;

    public DayFragment() {
    	matches = new ArrayList<>();
	}
	
	void setDate(Date d) {
    	this.date = d;
	}
	
	Date getDate() {
    	return date;
	}
	
	public View createView(LayoutInflater inflater, ViewGroup container){
		View view = inflater.inflate(R.layout.fragment_home_sub, container, false);
		matchList = (ListView) view.findViewById(R.id.match_list);

		matchAdapter = new MatchAdapter(getActivity(), R.layout.card_match, matches ,null);
		matchList.setAdapter(matchAdapter);

		matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(),MatchScreenActivity.class);
				int pos = (int)matchAdapter.getItemId(position);
				Match match = (Match) matches.get(pos);
				intent.putExtra("match",match);
				Log.i("Day", "" + match.getId());
				startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return createView(inflater, container);
	}
	
	public void addMatchSorted(Match match) {
		int i;
		int size = matches.size();
		for (i = 0; i < size; ++i) {
			// TODO: verify this
			if (matches.get(i).getKickoff_time().compareTo(match.getKickoff_time()) > 0) {
				break;
			}
		}
		matches.add(i, match);
	}
	
	public void notifyDataSetChanged() {
    	new Handler().post(new Runnable() {
			@Override
			public void run() {
				if (matchAdapter != null) {
					matchAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
}