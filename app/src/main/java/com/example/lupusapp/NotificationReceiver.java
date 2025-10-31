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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Lupus Daily Reminder")
                .setContentText("Time to check in and record your daily health journal.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
            Log.d("ALARM", "Notification shown successfully");
        } else {
            Log.w("ALARM", "Missing POST_NOTIFICATIONS permission — notification not shown");
        }

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

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent nextIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTrigger, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTrigger, pendingIntent);
        }
    }
}
