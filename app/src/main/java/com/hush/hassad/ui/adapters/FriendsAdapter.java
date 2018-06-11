package com.hush.hassad.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.player.User;

import java.util.ArrayList;

public class FriendsAdapter extends ArrayAdapter {
    Activity activity;
    int resource;
    ArrayList<User> friends;

    FriendHolder mFriendHolder;

    static class FriendHolder{
        ImageView friend_image;
        TextView friend_name;
		TextView points;
    }


    public FriendsAdapter(Activity activity, int resource, ArrayList<User> friends) {
        super(activity, resource,friends);
        this.activity = activity;
        this.resource = resource;
        this.friends = friends;
    }

    /*
    	TODO @saad
    	make an onclicklistener for each list item,
    	when that item is clicked
    		create a profile Fragment as pf
    		pf.update(friends[getPosition])
    		and open pf
     */


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // TODO: fix adapter
    
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, parent, false);
        }

        mFriendHolder = new FriendHolder();

        mFriendHolder.friend_name = convertView.findViewById(R.id.friend_name);
		mFriendHolder.points = convertView.findViewById(R.id.friends_points);
        mFriendHolder.friend_image = convertView.findViewById(R.id.friend_img);

        User friend = friends.get(position);

        mFriendHolder.friend_name.setText(friend.getInfo().getName());
		mFriendHolder.points.setText(Integer.toString(friend.getPoints()));
        mFriendHolder.friend_image.setImageResource(R.drawable.profile);

        return convertView;
    }
}
