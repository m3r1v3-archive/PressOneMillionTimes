package com.merive.pressonemilliontimes.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.merive.pressonemilliontimes.R;
import com.merive.pressonemilliontimes.activities.SplashActivity;

public class NotificationReceiver extends BroadcastReceiver {

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast
     *
     * @param context The Context in which the receiver is running
     * @param intent  The Intent being received
     * @see Context
     * @see Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat.from(context).notify(0, new NotificationCompat.Builder(context, "notifyPressOneMillionTimes")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(context.getResources().getColor(R.color.primary))
                .setContentTitle("The One Million is near")
                .setContentText("You have already pressed " + intent.getStringExtra("score") + " times")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE))
                .setAutoCancel(true).build());
    }
}
