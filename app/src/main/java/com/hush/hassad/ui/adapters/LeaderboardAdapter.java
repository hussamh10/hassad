package com.hush.hassad.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hush.hassad.R;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.util.DownloadImageTask;

import java.util.ArrayList;

public class LeaderboardAdapter extends ArrayAdapter {
    Activity activity;
    int resource;
    ArrayList<User> users;
    ViewHolder mHolder;

    static class ViewHolder{
        TextView rank;
        TextView name;
        TextView points;
        ImageView imgProfile;
    }

    public LeaderboardAdapter(Activity activity, int resource, ArrayList<User> users){
        super(activity, resource, users);
        this.activity = activity;
        this.resource = resource;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, parent, false);
        }

        mHolder = new ViewHolder();

        mHolder.name = convertView.findViewById(R.id.name);
        mHolder.rank = convertView.findViewById(R.id.rank);
        mHolder.points = convertView.findViewById(R.id.points);
        mHolder.imgProfile = convertView.findViewById(R.id.profile_img);

        User user = users.get(position);

        mHolder.name.setText(user.getInfo().getName());
        mHolder.rank.setText("" + (position + 1));
        mHolder.points.setText(Integer.toString(user.getPoints()));
        new DownloadImageTask(mHolder.imgProfile).execute(user.getInfo().getPhotoUrl());

        return convertView;
    }
}
