package com.example.lupusapp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.app.NotificationCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;


public class notifications_page extends Activity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);
        button = findViewById(R.id.btnAlarm);

        createNotificationChannel();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My notification", getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Builds the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(notifications_page.this, "My notification");
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setContentTitle("My Notification");
        builder.setContentText("Much longer text that cannot fit one line...");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        // Checks notification permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }

        // Shows the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(notifications_page.this);
        notificationManager.notify(1, builder.build());
    }
}