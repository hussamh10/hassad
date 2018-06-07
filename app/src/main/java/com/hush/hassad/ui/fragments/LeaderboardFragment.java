package com.hush.hassad.ui.fragments;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.dal.DAL;
import com.hush.hassad.ui.adapters.FriendsAdapter;
import com.hush.hassad.ui.adapters.LeaderboardAdapter;
import com.hush.hassad.ui.fragments.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class LeaderboardFragment extends Fragment {

	LeaderboardAdapter mLeaderboardAdapter;
	ArrayList<User> users;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_leaderboard, container,false);
		final ListView listView = view.findViewById(R.id.leaderboard_list);
		mLeaderboardAdapter = new LeaderboardAdapter(getActivity(), R.layout.card_leaderboard_item, users);
		listView.setAdapter(mLeaderboardAdapter);
		return view;
	}

	public void update(){
		DAL.getInstance().updateLeaderboard(this);
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
}