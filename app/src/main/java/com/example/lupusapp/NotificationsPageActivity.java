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
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotificationsPageActivity extends Activity {

    private static final String CHANNEL_ID = "lupus_notifications";

    private Switch switchNotifications;
    private Button btnPickTime, btnTestNotification;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);

        // Initialize UI components
        switchNotifications = findViewById(R.id.switchNotifications);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnTestNotification = findViewById(R.id.btnTestNotification);

        prefs = getSharedPreferences("lupus_prefs", MODE_PRIVATE);

        boolean enabled = prefs.getBoolean("notifications_enabled", false);
        int hour = prefs.getInt("notify_hour", 9);
        int minute = prefs.getInt("notify_minute", 0);

        switchNotifications.setChecked(enabled);
        btnPickTime.setEnabled(enabled);
        btnTestNotification.setEnabled(enabled);

        createNotificationChannel();

        // Notification switch
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply();
            btnPickTime.setEnabled(isChecked);
            btnTestNotification.setEnabled(isChecked);

            if (isChecked) {
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show();
            } else {
                cancelNotification();
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show();
            }
        });

        // Time picker button
        btnPickTime.setOnClickListener(v -> {
            TimePickerDialog picker = new TimePickerDialog(
                    this,
                    (TimePicker view, int selectedHour, int selectedMinute) -> {
                        prefs.edit()
                                .putInt("notify_hour", selectedHour)
                                .putInt("notify_minute", selectedMinute)
                                .apply();

                        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", false);
                        if (notificationsEnabled) {
                            scheduleDailyNotification(selectedHour, selectedMinute);
                            Toast.makeText(this,
                                    "Reminder set for " + selectedHour + ":" +
                                            String.format("%02d", selectedMinute),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this,
                                    "Notifications are off. Turn them on to activate reminders.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    },
                    hour, minute, true);
            picker.show();
        });

        // Test notification button
        btnTestNotification.setOnClickListener(v -> {
            boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", false);
            if (!notificationsEnabled) {
                Toast.makeText(this, "Notifications are off. Turn them on to test.", Toast.LENGTH_SHORT).show();
                return;
            }
            showTestNotification();
        });

        // Auto-reschedule if enabled
        if (enabled) {
            scheduleDailyNotification(hour, minute);
        }
    }

    // Create Notification Channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Daily lupus reminders and test notifications");
            channel.enableVibration(true);
            channel.enableLights(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // Show test notification (clickable)
    private void showTestNotification() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    1
            );
            return;
        }

        // Open LoginPageActivity when tapped
        Intent openIntent = new Intent(this, LoginPageActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Lupus Reminder")
                .setContentText("This is a test notification.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        int notificationId = (int) System.currentTimeMillis();
        NotificationManagerCompat.from(this).notify(notificationId, builder.build());
    }

    // Handle notification permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showTestNotification();
        } else {
            Toast.makeText(this, "Permission denied. Cannot show notification.", Toast.LENGTH_SHORT).show();
        }
    }

    // Schedule a daily notification
    private void scheduleDailyNotification(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
                Toast.makeText(this, "Please allow 'Exact alarms' access for Lupus App", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Log.d("ALARM", "Scheduling daily notification for " + hour + ":" + minute);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
            Log.d("ALARM", "Time already passed today — scheduling for tomorrow");
        }

        long triggerAt = calendar.getTimeInMillis();
        Log.d("ALARM", "Trigger timestamp = " + triggerAt + " (" + new java.util.Date(triggerAt) + ")");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent);
            Log.d("ALARM", "Using setExactAndAllowWhileIdle()");
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent);
            Log.d("ALARM", "Using setExact()");
        }

        Toast.makeText(this, "Reminder scheduled for " +
                String.format("%02d:%02d", hour, minute), Toast.LENGTH_SHORT).show();
    }

    // Cancel existing scheduled notification (called if notis r off)
    private void cancelNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}
