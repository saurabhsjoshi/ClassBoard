package com.sau.classboard.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.magnet.mmx.client.api.MMX;
import com.magnet.mmx.client.api.MMXMessage;
import com.sau.classboard.R;
import com.sau.classboard.activity.HomeActivity;

/**
 * Created by saurabh on 2015-10-20.
 */
public class MessageBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        showNotification(context);
        MMX.registerListener(new MMX.EventListener() {
            public boolean onMessageReceived(MMXMessage mmxMessage) {
                showNotification(context);
                return false;
            }
        });
    }

    private void showNotification(Context context){
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, HomeActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("New Message")
                        .setContentText("Naya message aaya hai lulz");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
