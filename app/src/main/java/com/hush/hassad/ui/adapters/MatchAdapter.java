package com.hush.hassad.ui.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.Utils;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.predictions.MatchPrediction;
import com.hush.hassad.controller.predictions.Prediction;
import com.hush.hassad.util.DownloadImageTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Saad Mujeeb on 31/3/2018.
 */

public class MatchAdapter extends ArrayAdapter {

	Activity activity;
	int resource;
	ArrayList<Match> matches;
	MatchHolder matchHolder;

	static class MatchHolder{
		CircleImageView home_team_img;
		TextView home_team_name;
		TextView pred_home_score;
		TextView home_score;

		CircleImageView away_team_img;
		TextView away_team_name;
		TextView pred_away_score;
		TextView away_score;

		TextView match_time;

		LinearLayout result;
	}


	public MatchAdapter(Activity activity, int resource, ArrayList<Match> matches, ArrayList<Long> quantities){
		super(activity,resource,matches);
		this.activity = activity;
		this.matches = matches;
		this.resource = resource;
	}
	
	public ArrayList<Match> getMatches() {
		return matches;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(resource, parent, false);

			matchHolder = new MatchHolder();

			matchHolder.home_team_img = (CircleImageView) convertView.findViewById(R.id.home_team_img);
			matchHolder.home_team_name = (TextView) convertView.findViewById(R.id.home_team_name);
			matchHolder.pred_home_score = (TextView) convertView.findViewById(R.id.pred_home_score);
			matchHolder.home_score = (TextView) convertView.findViewById(R.id.home_score);

			matchHolder.away_team_img = (CircleImageView) convertView.findViewById(R.id.away_team_img);
			matchHolder.away_team_name = (TextView) convertView.findViewById(R.id.away_team_name);
			matchHolder.pred_away_score = (TextView) convertView.findViewById(R.id.pred_away_score);
			matchHolder.away_score = (TextView) convertView.findViewById(R.id.away_score);

			matchHolder.match_time = (TextView) convertView.findViewById(R.id.match_time);
			matchHolder.result = (LinearLayout) convertView.findViewById(R.id.result);
			convertView.setTag(matchHolder);
		} else {
			matchHolder = (MatchHolder) convertView.getTag();
		}

		Match match = matches.get(position);

		/*
			Getting prediction for match if any
		 */

		MatchPrediction prediction;
		//TODO prediction
/*

		boolean isPredicted = Manager.getInstance().isPredicted(match);
		if (isPredicted){
			try{
				prediction = Manager.getInstance().getPrediction(match);
			}
			catch (Exception e){
				Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
*/

		//TODO @hussam test the predcited score in card


		byte [] bytes = match.getHome().getImage_url();
		Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		matchHolder.home_team_img.setImageBitmap(bmp);
		//String photoUrl = match.getHome().getImage_url();
		//new DownloadImageTask(matchHolder.home_team_img).execute(photoUrl);
		matchHolder.home_team_name.setText(match.getHome().getName().toString());

		bytes = match.getAway().getImage_url();
		bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		matchHolder.away_team_img.setImageBitmap(bmp);
		//photoUrl = match.getAway().getImage_url();
		//new DownloadImageTask(matchHolder.away_team_img).execute(photoUrl);
		matchHolder.away_team_name.setText(match.getAway().getName().toString());

		//TODO prediction
		/*if(Manager.getInstance().isPredicted(match)){
			MatchPrediction pred = null;
			try {
				pred = Manager.getInstance().getPrediction(match);
			} catch (Exception e) {
				Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			matchHolder.pred_home_score.setText(Integer.toString(pred.getPredicted_result().getHome_score()));
			matchHolder.pred_away_score.setText(Integer.toString(pred.getPredicted_result().getAway_score()));
		}
*/
		if(!match.isEnded()) {
			matchHolder.match_time.setVisibility(View.VISIBLE);
			matchHolder.result.setVisibility(View.GONE);

			String time = Utils.getTimeString(match.getKickoff_time());
			matchHolder.match_time.setText(time);
		}
		else {
			matchHolder.match_time.setVisibility(View.GONE);
			matchHolder.result.setVisibility(View.VISIBLE);

			matchHolder.home_score.setText(Integer.toString(match.getResult().getHome_score()));
			matchHolder.away_score.setText(Integer.toString(match.getResult().getAway_score()));
		}

		return convertView;
	}

}
