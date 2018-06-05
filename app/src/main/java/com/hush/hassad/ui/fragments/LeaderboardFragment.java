package com.hush.hassad.ui.fragments;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hush.hassad.R;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.dal.DAL;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

	ArrayList<User> users;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_leaderboard, container,false);
	}

	public void update(){
		users = new ArrayList<>();
		DAL.getInstance().updateLeaderboard(this);
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
}
