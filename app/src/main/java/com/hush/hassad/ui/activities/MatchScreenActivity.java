package com.hush.hassad.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.BiMap;
import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.Utils;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.results.MatchResult;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.MatchPrediction;
import com.hush.hassad.dal.DAL;

import java.util.UUID;

public class MatchScreenActivity extends AppCompatActivity {

	Match match;
	User user;
	MatchPrediction predicted;

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

		Button submit = (Button) findViewById(R.id.btn_submit);

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

		byte [] bytes = match.getHome().getImage_url();
		Bitmap bmp =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		home_team_img.setImageBitmap(bmp);
		home_team_name.setText(match.getHome().getName().toString());

		bytes = match.getAway().getImage_url();
		bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		away_team_img.setImageBitmap(bmp);
		away_team_name.setText(match.getAway().getName().toString());

		/*if(Manager.getInstance().isPredicted(match)){
			MatchPrediction pred = null;
			try {
				pred = Manager.getInstance().getPrediction(match);
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			pred_home_score.setText(Integer.toString(pred.getPredicted_result().getHome_score()));
			pred_away_score.setText(Integer.toString(pred.getPredicted_result().getAway_score()));
		}*/

		if(!match.isEnded()) {
			String time = Utils.getTimeString(match.getKickoff_time());
			match_time.setText(time);
		}
		else {

			home_score.setText(Integer.toString(match.getResult().getHome_score()));
			away_score.setText(Integer.toString(match.getResult().getAway_score()));
		}

		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				EditText temp = (EditText) findViewById(R.id.et_home_score);
				final int predicted_home_score = Integer.parseInt(temp.getText().toString());

				temp = (EditText) findViewById(R.id.et_away_score);
				final int predicted_away_score = Integer.parseInt(temp.getText().toString());

				MatchPrediction prediction;
				if (predicted_home_score >= predicted_away_score){
					prediction = Manager.getInstance().createMatchPrediction(match, predicted_home_score, predicted_away_score, match.getHome());
				}
				else{
					prediction = Manager.getInstance().createMatchPrediction(match, predicted_home_score, predicted_away_score, match.getAway());
				}
				try{
					Manager.getInstance().submitMatchPrediction(prediction);
				}
				catch (Exception e){
					Toast.makeText(MatchScreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
}
