package com.hush.hassad.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.predictions.GroupPrediction;
import com.hush.hassad.ui.adapters.GroupPredictionsAdapter;
import java.util.ArrayList;

public class GroupFragment extends Fragment {

	GroupPredictionsAdapter groupPredictionsAdapter;
	private ArrayList<GroupPrediction> predictedGroups;

	public GroupFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_group_sub, container, false);
		ListView groupsList = (ListView) view.findViewById(R.id.groups_list);

		groupPredictionsAdapter = new GroupPredictionsAdapter(getActivity(), R.layout.card_predicted_group, predictedGroups ,null);
		groupsList.setAdapter(groupPredictionsAdapter);

		return view;
	}

	public void setGroupPredictions(){
		this.predictedGroups = predictedGroups;
	}

	public void update(){
		predictedGroups = Manager.getInstance().getPredictedGroups(Manager.getInstance().getPlayingUser());
	}

}
