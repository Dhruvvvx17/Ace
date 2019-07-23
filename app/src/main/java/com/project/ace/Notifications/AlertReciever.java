package com.project.ace.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Date;

public class AlertReciever extends BroadcastReceiver {

    String title,description;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        title = extras.getString("Title");
        description = extras.getString("Description");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title,description);

        int num = (int)((new Date().getTime() / 1000L ) % Integer.MAX_VALUE);   //Create new notif id for every new reminder
        Log.d("notif","New Notif id: "+num);
        notificationHelper.getManager().notify(num,nb.build());
    }
}
