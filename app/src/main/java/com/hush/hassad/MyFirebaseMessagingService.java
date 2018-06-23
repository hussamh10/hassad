package com.hush.hassad;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hush.hassad.ui.activities.MainActivity;

import static com.hush.hassad.App.CHANNEL_1_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
	public MyFirebaseMessagingService() {
	}

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);

		sendNotification(remoteMessage.getNotification().getBody());
	}


	public void sendNotification(String messageBody)
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT);

		Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
				.setSmallIcon(R.mipmap.ic_launcher_round)
				.setContentTitle("Hassad")
				.setContentText(messageBody)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setCategory(NotificationCompat.CATEGORY_MESSAGE)
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent).build();


		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(1, notification);

	}

}
