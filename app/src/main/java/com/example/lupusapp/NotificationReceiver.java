package com.example.lupusapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "lupus_notifications";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ALARM", "NotificationReceiver triggered!");

        // Build PendingIntent for LoginPage (gives notifications ability to be clicked n stuff)
        Intent openIntent = new Intent(context, LoginActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build & show the test notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Lupus Daily Reminder")
                .setContentText("Time to check in and record your daily health journal.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context)
                    .notify((int) System.currentTimeMillis(), builder.build());
            Log.d("ALARM", "Notification shown successfully");
        } else {
            Log.w("ALARM", "Missing POST_NOTIFICATIONS permission — notification not shown");
        }

        // Reschedule next daily notification
        SharedPreferences prefs = context.getSharedPreferences("lupus_prefs", Context.MODE_PRIVATE);
        int hour = prefs.getInt("notify_hour", 9);
        int minute = prefs.getInt("notify_minute", 0);

        Calendar next = Calendar.getInstance();
        next.set(Calendar.HOUR_OF_DAY, hour);
        next.set(Calendar.MINUTE, minute);
        next.set(Calendar.SECOND, 0);
        next.set(Calendar.MILLISECOND, 0);
        next.add(Calendar.DATE, 1);

        long nextTrigger = next.getTimeInMillis();
        Log.d("ALARM", "Rescheduling next notification for " + new java.util.Date(nextTrigger));

        // Schedule next alarm with AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent nextIntent = new Intent(context, NotificationReceiver.class);

        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTrigger, nextPendingIntent);
            Log.d("ALARM", "Next alarm set with setExactAndAllowWhileIdle()");
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTrigger, nextPendingIntent);
            Log.d("ALARM", "Next alarm set with setExact()");
        }
    }
}
