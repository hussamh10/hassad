package com.hush.hassad.ui.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.competition.Group;
import com.hush.hassad.controller.competition.Team;
import com.hush.hassad.controller.predictions.GroupPrediction;
import java.util.ArrayList;

/**
 * Created by Saad Mujeeb on 3/4/2018.
 */

public class GroupPredictionsAdapter extends ArrayAdapter {

	Activity activity;
	int resource;
	ArrayList<GroupPrediction> groups;
	GroupHolder groupHolder;

	static class GroupHolder{
		TextView predictedGroupNum;
		ImageView team1Img;
		TextView team1Name;
		ImageView team2Img;
		TextView team2Name;
		ImageView team3Img;
		TextView team3Name;
		ImageView team4Img;
		TextView team4Name;

		CheckBox checkBox1;
		CheckBox checkBox2;
		CheckBox checkBox3;
		CheckBox checkBox4;
	}

	public GroupPredictionsAdapter(Activity activity, int resource, ArrayList<GroupPrediction> groups, ArrayList<Long> quantities){
		super(activity,resource,groups);
		this.activity = activity;
		this.groups = groups;
		this.resource = resource;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(resource, parent, false);
		}

		groupHolder = new GroupHolder();

		View predictedTeam1 = convertView.findViewById(R.id.predicted_team1);
		View predictedTeam2 = convertView.findViewById(R.id.predicted_team2);
		View predictedTeam3 = convertView.findViewById(R.id.predicted_team3);
		View predictedTeam4 = convertView.findViewById(R.id.predicted_team4);

		predictedTeam1.setBackgroundColor(Color.GREEN);
		predictedTeam2.setBackgroundColor(Color.GREEN);

		groupHolder.predictedGroupNum = (TextView) convertView.findViewById(R.id.predicted_group_num);
		groupHolder.team1Img = (ImageView) predictedTeam1.findViewById(R.id.team_img);
		groupHolder.team1Name = (TextView) predictedTeam1.findViewById((R.id.team_name));
		groupHolder.team2Img = (ImageView) predictedTeam2.findViewById(R.id.team_img);
		groupHolder.team2Name = (TextView) predictedTeam2.findViewById((R.id.team_name));
		groupHolder.team3Img = (ImageView) predictedTeam3.findViewById(R.id.team_img);
		groupHolder.team3Name = (TextView) predictedTeam3.findViewById((R.id.team_name));
		groupHolder.team4Img = (ImageView) predictedTeam4.findViewById(R.id.team_img);
		groupHolder.team4Name = (TextView) predictedTeam4.findViewById((R.id.team_name));

		groupHolder.checkBox1 = (CheckBox) predictedTeam1.findViewById(R.id.team_selected);
		groupHolder.checkBox1.setVisibility(View.GONE);
		groupHolder.checkBox2 = (CheckBox) predictedTeam2.findViewById(R.id.team_selected);
		groupHolder.checkBox2.setVisibility(View.GONE);
		groupHolder.checkBox3 = (CheckBox) predictedTeam3.findViewById(R.id.team_selected);
		groupHolder.checkBox3.setVisibility(View.GONE);
		groupHolder.checkBox4 = (CheckBox) predictedTeam4.findViewById(R.id.team_selected);
		groupHolder.checkBox4.setVisibility(View.GONE);

		GroupPrediction groupPrediction = groups.get(position);

		Group group = Manager.getInstance().getGroup(groupPrediction.getPredicted_result().getGroup());

		groupHolder.predictedGroupNum.setText(group.getName().toString());
		groupHolder.team1Img.setImageResource(R.drawable.manchester_united);
		groupHolder.team1Name.setText(groupPrediction.getPredicted_result().getQualifying_1().getName().toString());
		groupHolder.team2Img.setImageResource(R.drawable.manchester_united);
		groupHolder.team2Name.setText(groupPrediction.getPredicted_result().getQualifying_2().getName().toString());

		String team1 = group.getTeams().get(0).getName().toString();
		String team2 = group.getTeams().get(1).getName().toString();
		String team3 = group.getTeams().get(2).getName().toString();
		String team4 = group.getTeams().get(3).getName().toString();
		String qualifying1 = groupPrediction.getPredicted_result().getQualifying_1().getName().toString();
		String qualifying2 = groupPrediction.getPredicted_result().getQualifying_2().getName().toString();

		String t3 = "";
		String t4 = "";

		if(!team1.equals(qualifying1) && !team1.equals(qualifying2))
			t3 = team1;
		else if(!team2.equals(qualifying1) && !team2.equals(qualifying2))
			t3 = team2;
		else if(!team3.equals(qualifying1)&& !team3.equals(qualifying2))
			t3 = team3;
		else if(!team4.equals(qualifying1)&& !team4.equals(qualifying2))
			t3 = team4;

		if(!team1.equals(qualifying1) && !team1.equals(qualifying2) && !team1.equals(t3))
			t4 = team1;
		else if(!team2.equals(qualifying1) && !team2.equals(qualifying2) && !team2.equals(t3))
			t4 = team2;
		else if(!team3.equals(qualifying1)&& !team3.equals(qualifying2) && !team3.equals(t3))
			t4 = team3;
		else if(!team4.equals(qualifying1)&& !team4.equals(qualifying2) && !team4.equals(t3))
			t4 = team4;

		groupHolder.team3Img.setImageResource(R.drawable.manchester_united);
		groupHolder.team3Name.setText(t3);
		groupHolder.team4Img.setImageResource(R.drawable.manchester_united);
		groupHolder.team4Name.setText(t4);

		return convertView;
	}
}
