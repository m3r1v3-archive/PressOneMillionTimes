package com.merive.press1mtimes;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyPress1MTimes")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(context.getResources().getColor(R.color.red))
                .setContentTitle("Soon you will reach your goal...")
                .setContentText("You only pressed " + MainActivity.getScoreForNotifications() + " times")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(135, builder.build());
    }
}
