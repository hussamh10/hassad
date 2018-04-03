package com.hush.hassad.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.Utils;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.predictions.MatchPrediction;

import java.util.ArrayList;

/**
 * Created by Saad Mujeeb on 1/4/2018.
 */

public class MatchesPredictedAdapter extends ArrayAdapter {

	Activity activity;
	int resource;
	ArrayList<MatchPrediction> predictedMatches;
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


	public MatchesPredictedAdapter(Activity activity, int resource, ArrayList<MatchPrediction> predictedMatches, ArrayList<Long> quantities){
		super(activity,resource,predictedMatches);
		this.activity = activity;
		this.predictedMatches = predictedMatches;
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

		MatchPrediction predictedMatch = predictedMatches.get(position);
		matchHolder.home_team_img.setImageResource(R.drawable.manchester_united);
		Match match = Manager.getInstance().getMatch(predictedMatch.getPredicted_result().getMatch());

		matchHolder.home_team_name.setText(match.getHome().getName().toString());
		matchHolder.pred_home_score.setText(Integer.toString(predictedMatch.getPredicted_result().getHome_score()));

		matchHolder.away_team_img.setImageResource(R.drawable.chelsea);
		matchHolder.away_team_name.setText(match.getAway().getName().toString());
		matchHolder.pred_away_score.setText(Integer.toString(predictedMatch.getPredicted_result().getAway_score()));

		if(!match.isEnded()) {
			String time = Utils.getTimeString(match.getKickoff_time());
			matchHolder.match_time.setText(time);
		}
		else{
			matchHolder.home_score.setText(Integer.toString(match.getResult().getHome_score()));
			matchHolder.away_score.setText(Integer.toString(match.getResult().getAway_score()));
			matchHolder.home_score.setVisibility(View.VISIBLE);
			matchHolder.away_score.setVisibility(View.VISIBLE);
			matchHolder.match_time.setVisibility((View.GONE));
		}

		return convertView;
	}
}
