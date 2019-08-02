package com.project.ace.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    String title,description;
    int id;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        assert extras != null;
        title = extras.getString("Title");
        description = extras.getString("Description");
        id = extras.getInt("Notification_id");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title,description,context);
        notificationHelper.getManager().notify(id,nb.build());
    }
}
