package com.diligencia.BirthdayReminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ReminderService extends IntentService {
    private static final int NOTIF_ID = 1;

    public ReminderService(){
        super("ReminderService");
    }

    @SuppressWarnings("deprecation")
	@Override
      protected void onHandleIntent(Intent intent) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long when = System.currentTimeMillis();         // notification time
        Notification notification = new Notification(R.drawable.cake3, "reminder", when);
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent , 0);
        notification.setLatestEventInfo(getApplicationContext(), "It's about time", "You should open the app now", contentIntent);
        nm.notify(NOTIF_ID, notification);
    }

}