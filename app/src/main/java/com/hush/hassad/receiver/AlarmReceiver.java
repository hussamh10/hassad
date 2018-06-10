package com.hush.hassad.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.hush.hassad.R;
import com.hush.hassad.dal.DAL;
import com.hush.hassad.ui.activities.MainActivity;

import java.util.Calendar;
import java.util.Date;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static com.hush.hassad.App.CHANNEL_1_ID;

/**
 * Created by Saad Mujeeb on 6/6/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
	public int count = 0;
	private Context context;
	private NotificationManagerCompat notificationManager;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if(isNetworkAvailable()) {
			final Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			Date date = cal.getTime();
			DAL.getInstance().isMatch(this, date);
		}
	}

	public void setCount(int count){
		this.count = count;
	}

	public void sendNotification()
	{
		Intent notificationIntent = new Intent(context, MainActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(notificationIntent);

		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		if(count != 0)
		{
			notificationManager = NotificationManagerCompat.from(context);
			Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
					.setSmallIcon(R.mipmap.ic_launcher_round)
					.setContentText("Match Today")
					.setTicker("Match Today!")
					.setPriority(NotificationCompat.PRIORITY_HIGH)
					.setCategory(NotificationCompat.CATEGORY_MESSAGE)
					.setContentIntent(pendingIntent).build();

			notificationManager.notify(1, notification);
		}
		else{
			notificationManager = NotificationManagerCompat.from(context);
			Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
					.setSmallIcon(R.mipmap.ic_launcher_round)
					.setContentText("No Match Today!")
					.setTicker("No Matches Toay!")
					.setPriority(NotificationCompat.PRIORITY_HIGH)
					.setCategory(NotificationCompat.CATEGORY_MESSAGE)
					.setContentIntent(pendingIntent).build();

			notificationManager.notify(1, notification);
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
