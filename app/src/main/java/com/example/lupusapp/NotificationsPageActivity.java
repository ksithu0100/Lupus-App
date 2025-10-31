package com.example.lupusapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotificationsPageActivity extends Activity {

    private Switch switchNotifications;
    private Button btnPickTime, btnTestNotification;
    private SharedPreferences prefs;

    private static final String CHANNEL_ID = "lupus_notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);

        switchNotifications = findViewById(R.id.switchNotifications);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnTestNotification = findViewById(R.id.btnTestNotification);

        prefs = getSharedPreferences("lupus_prefs", MODE_PRIVATE);

        boolean enabled = prefs.getBoolean("notifications_enabled", false);
        int hour = prefs.getInt("notify_hour", 9);
        int minute = prefs.getInt("notify_minute", 0);

        switchNotifications.setChecked(enabled);
        btnPickTime.setEnabled(enabled);

        createNotificationChannel();

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply();
            btnPickTime.setEnabled(isChecked);
            if (isChecked) {
                scheduleDailyNotification(hour, minute);
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show();
            } else {
                cancelNotification();
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show();
            }
        });

        btnPickTime.setOnClickListener(v -> {
            TimePickerDialog picker = new TimePickerDialog(
                    this,
                    (TimePicker view, int selectedHour, int selectedMinute) -> {
                        prefs.edit()
                                .putInt("notify_hour", selectedHour)
                                .putInt("notify_minute", selectedMinute)
                                .apply();
                        scheduleDailyNotification(selectedHour, selectedMinute);
                        Toast.makeText(this, "Reminder set for " + selectedHour + ":" + String.format("%02d", selectedMinute), Toast.LENGTH_SHORT).show();
                    },
                    hour, minute, true);
            picker.show();
        });

        btnTestNotification.setOnClickListener(v -> showTestNotification());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void showTestNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Lupus Reminder")
                .setContentText("This is a test notification.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }

        NotificationManagerCompat.from(this).notify(1, builder.build());
    }

    private void scheduleDailyNotification(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    private void cancelNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}
