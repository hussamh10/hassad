package com.hush.hassad.ui.fragments;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.competition.Group;

import java.util.ArrayList;
import java.util.Collections;

public class TableFragment extends Fragment {

	ArrayList<Group> groups;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_table, container,false);
		// TODO load info from groups
	}

	public void update(){
		groups = Manager.getGroups();
		for(Group group : groups){
			group.sort();
		}
	}
}
