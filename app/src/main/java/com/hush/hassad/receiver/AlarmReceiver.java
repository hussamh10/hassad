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
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.hush.hassad.R;
import com.hush.hassad.ui.activities.MainActivity;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static com.hush.hassad.App.CHANNEL_1_ID;

/**
 * Created by Saad Mujeeb on 6/6/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
	//private static final String CHANNEL_ID = "com.singhajit.notificationDemo.channelId";

	private NotificationManagerCompat notificationManager;
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent notificationIntent = new Intent(context, MainActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(notificationIntent);

		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		/*Notification notification = new Notification.Builder(context).
				setTicker("Mangwalo Pakistan").
				setContentTitle("Mangwalo Pakistan").
				setContentText("Dear customer your order is on the way!").
				setSmallIcon(R.mipmap.ic_launcher_round).
				setContentIntent(pendingIntent).getNotification();
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
		try {
			Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notificationUri);
			r.play();
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		notificationManager = NotificationManagerCompat.from(context);
		Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
				.setSmallIcon(R.mipmap.ic_launcher_round)
				.setContentText("New Notification From Demo App..")
				.setTicker("New Message Alert!")
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setCategory(NotificationCompat.CATEGORY_MESSAGE)
				.setContentIntent(pendingIntent).build();

		notificationManager.notify(1, notification);
	}
}
