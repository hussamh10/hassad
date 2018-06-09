package com.hush.hassad.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hush.hassad.R;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.dal.DAL;
import com.hush.hassad.ui.adapters.MatchAdapter;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

	private MatchAdapter matchAdapter;
	private ArrayList<Match> matches;

	public ScheduleActivity() {
		this.matches = new ArrayList<>();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		DAL.getInstance().updateSchedule(this);

		ListView schedule_list = (ListView) findViewById(R.id.schedule_list);

		matchAdapter = new MatchAdapter(this ,R.layout.card_match, matches ,null);
		schedule_list.setAdapter(matchAdapter);
	}

	public void addMatch(Match m) {
		//TODO: insert sorted
		this.matches.add(m);
		if (matchAdapter != null) {
			matchAdapter.notifyDataSetChanged();
		}
	}
}
