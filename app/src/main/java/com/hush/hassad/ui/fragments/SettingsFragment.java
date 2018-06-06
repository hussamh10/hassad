package com.hush.hassad.ui.fragments;

import android.app.Fragment;
import android.app.Notification;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hush.hassad.R;

import static com.hush.hassad.App.CHANNEL_1_ID;

public class SettingsFragment extends Fragment {

	private NotificationManagerCompat notificationManager;
	Button button;
	EditText title,body;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container, false);

		title = (EditText) view.findViewById(R.id.title);
		body = (EditText) view.findViewById(R.id.body);
		button = (Button) view.findViewById(R.id.send);


		notificationManager = NotificationManagerCompat.from(getActivity());

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String t = title.getText().toString();
				String b = body.getText().toString();

				Notification notification = new NotificationCompat.Builder(getActivity(), CHANNEL_1_ID)
						.setSmallIcon(R.mipmap.ic_launcher_round)
						.setContentTitle(t)
						.setContentText(b)
						.setPriority(NotificationCompat.PRIORITY_HIGH)
						.setCategory(NotificationCompat.CATEGORY_MESSAGE)
						.build();

				notificationManager.notify(1, notification);
			}
		});

		return view;
	}
}
