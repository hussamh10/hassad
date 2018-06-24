package com.hush.hassad.ui.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hush.hassad.R;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.util.DownloadImageGeneratorTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardAdapter extends ArrayAdapter {
    Activity activity;
    int resource;
    ArrayList<User> users;
    private ArrayList<Bitmap> userImages;
    ViewHolder mHolder;

    static class ViewHolder{
        TextView rank;
        TextView name;
        TextView points;
        CircleImageView imgProfile;
    }

    public LeaderboardAdapter(Activity activity, int resource, ArrayList<User> users) {
        super(activity, resource, users);
        this.activity = activity;
        this.resource = resource;
        this.users = users;
        this.userImages = new ArrayList<>();
        
        new DownloadImageGeneratorTask(new DownloadImageGeneratorTask.IDownloaded() {
            @Override
            public void downloaded(final Bitmap bmp) {
                userImages.add(bmp);
                notifyDataSetChanged();
            }
        }).execute(users.toArray(new User[0]));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // TODO: fix adapter
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, parent, false);

            mHolder = new ViewHolder();

            mHolder.name = convertView.findViewById(R.id.name);
            mHolder.rank = convertView.findViewById(R.id.rank);
            mHolder.points = convertView.findViewById(R.id.points);
            mHolder.imgProfile = convertView.findViewById(R.id.profile_img);
            convertView.setTag(mHolder);
        }
        else {
            mHolder = (ViewHolder) convertView.getTag();
        }


        User user = users.get(position);

        mHolder.name.setText(user.getInfo().getName());
        mHolder.rank.setText(Integer.toString(position + 1));
        mHolder.points.setText(Integer.toString(user.getPoints()));
        
        try {
            mHolder.imgProfile.setImageBitmap(userImages.get(position));
        } catch (Exception e) {
            mHolder.imgProfile.setImageResource(R.drawable.profile);
        }
        
        return convertView;
    }
    
    
}
