package com.hush.hassad.ui.fragments;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.ui.adapters.FriendsAdapter;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {

	FriendsAdapter mFriendsAdapter;

	ArrayList<User> friends;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friends, container,false);
		ListView friendsList = view.findViewById(R.id.friends_list);

		mFriendsAdapter = new FriendsAdapter(getActivity(), R.layout.card_friend, friends);
		friendsList.setAdapter(mFriendsAdapter);

		//matchesPredictedAdapter = new MatchesPredictedAdapter(getActivity(), R.layout.card_match, predictedMatches ,null);
		return view;
	}

	public void update(){
		friends = Manager.getInstance().getFriends();
	}
}
