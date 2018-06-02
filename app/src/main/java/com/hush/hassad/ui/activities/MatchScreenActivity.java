package com.hush.hassad.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.Utils;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.predictions.MatchPrediction;

public class MatchScreenActivity extends AppCompatActivity {

	Match match;
	ImageView home_team_img;
	TextView home_team_name;
	TextView pred_home_score;
	TextView home_score;

	ImageView away_team_img;
	TextView away_team_name;
	TextView pred_away_score;
	TextView away_score;

	TextView match_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_screen);

		match = (Match) getIntent().getSerializableExtra("match");

		home_team_img = (ImageView) findViewById(R.id.home_team_img);
		home_team_name = (TextView) findViewById(R.id.home_team_name);
		pred_home_score = (TextView) findViewById(R.id.pred_home_score);
		home_score = (TextView) findViewById(R.id.home_score);

		away_team_img = (ImageView) findViewById(R.id.away_team_img);
		away_team_name = (TextView) findViewById(R.id.away_team_name);
		pred_away_score = (TextView) findViewById(R.id.pred_away_score);
		away_score = (TextView) findViewById(R.id.away_score);

		match_time = (TextView) findViewById(R.id.match_time);

		home_team_img.setImageResource(R.drawable.manchester_united);
		home_team_name.setText(match.getHome().getName().toString());

		away_team_img.setImageResource(R.drawable.chelsea);
		away_team_name.setText(match.getAway().getName().toString());

		if(Manager.getInstance().isPredicted(match)){
			MatchPrediction pred = null;
			try {
				pred = Manager.getInstance().getPrediction(match);
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			pred_home_score.setText(Integer.toString(pred.getPredicted_result().getHome_score()));
			pred_away_score.setText(Integer.toString(pred.getPredicted_result().getAway_score()));
		}

		if(!match.isEnded()) {
			String time = Utils.getTimeString(match.getKickoff_time());
			match_time.setText(time);
		}
		else {

			home_score.setText(Integer.toString(match.getResult().getHome_score()));

			away_score.setText(Integer.toString(match.getResult().getAway_score()));
		}

	}
}
