package com.matrix.visitingcard.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.matrix.visitingcard.GCMNotificationActionActivity;
import com.matrix.visitingcard.ListMyVCRActivity;
import com.matrix.visitingcard.R;
import com.matrix.visitingcard.user.User;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()
				&& GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
						.equals(messageType))
			showNotification(extras);
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void showNotification(Bundle extras) {
		if (User.isSignedIn(getApplicationContext())) {
			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, ListMyVCRActivity.class), 0);
			String title = extras.getString("title");
			String msg = extras.getString("message");
			if (msg == null)
				msg = title;
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(title)
					.setStyle(
							new NotificationCompat.BigTextStyle().bigText(msg))
					.setContentText(msg);
			mBuilder.setContentIntent(contentIntent);
			mBuilder.setSound(RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			Notification notification = mBuilder.build();
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(NOTIFICATION_ID, notification);
		}
	}
}