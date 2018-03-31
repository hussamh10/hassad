package com.hush.hassad.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hush.hassad.R;
import com.hush.hassad.controller.Utils;
import com.hush.hassad.controller.competition.Match;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Saad Mujeeb on 31/3/2018.
 */

public class MatchAdapter extends ArrayAdapter {

	Activity activity;
	int resource;
	ArrayList<Match> matches;
	MatchHolder matchHolder;

	static class MatchHolder{
		ImageView home_team_img;
		TextView home_team_name;
		TextView pred_home_score;
		TextView home_score;

		ImageView away_team_img;
		TextView away_team_name;
		TextView pred_away_score;
		TextView away_score;

		TextView match_time;
	}


	public MatchAdapter(Activity activity, int resource, ArrayList<Match> matches, ArrayList<Long> quantities){
		super(activity,resource,matches);
		this.activity = activity;
		this.matches = matches;
		this.resource = resource;
	}


	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(resource, parent, false);
		}

		matchHolder = new MatchHolder();


		matchHolder.home_team_img = (ImageView) convertView.findViewById(R.id.home_team_img);
		matchHolder.home_team_name = (TextView) convertView.findViewById(R.id.home_team_name);
		matchHolder.pred_home_score = (TextView) convertView.findViewById(R.id.pred_home_score);
		matchHolder.home_score = (TextView) convertView.findViewById(R.id.home_score);

		matchHolder.away_team_img = (ImageView) convertView.findViewById(R.id.away_team_img);
		matchHolder.away_team_name = (TextView) convertView.findViewById(R.id.away_team_name);
		matchHolder.pred_away_score = (TextView) convertView.findViewById(R.id.pred_away_score);
		matchHolder.away_score = (TextView) convertView.findViewById(R.id.away_score);

		matchHolder.match_time = (TextView) convertView.findViewById(R.id.match_time);


		Match match = matches.get(position);
		matchHolder.home_team_img.setImageResource(R.drawable.manchester_united);
		matchHolder.home_team_name.setText(match.getHome().getName().toString());

		matchHolder.away_team_img.setImageResource(R.drawable.chelsea);
		matchHolder.away_team_name.setText(match.getAway().getName().toString());

		if(!match.isEnded()) {
			String time = Utils.getTimeString(match.getKickoff_time());
			matchHolder.match_time.setText(time);
		}

		return convertView;
	}
}
