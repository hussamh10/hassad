package com.hush.hassad.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.ui.adapters.FriendsAdapter;
import com.hush.hassad.ui.fragments.profile.ProfileFragment;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {

	FriendsAdapter mFriendsAdapter;

	ArrayList<User> friends;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friends, container,false);
		final ListView friendsList = view.findViewById(R.id.friends_list);

		mFriendsAdapter = new FriendsAdapter(getActivity(), R.layout.card_friend, friends);
		friendsList.setAdapter(mFriendsAdapter);

		friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User friend = friends.get(position);

				ProfileFragment fragment = new ProfileFragment();
				fragment.update(friend);

				getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.content_frame, fragment)
						.addToBackStack(null)
						.commit();
			}
		});
		
		return view;
	}

	public void update(){
		friends = Manager.getInstance().getFriends();
	}
}
