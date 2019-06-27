package com.navisens.demo.android_app_helloworld;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.app.NotificationChannel;

import androidx.annotation.NonNull;

public class MotionDnaForegroundService extends Service {

    static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        int myTid = android.os.Process.myTid();
        android.os.Process.setThreadPriority(myTid, android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        Intent mainIntent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                NOTIFICATION_ID,
                mainIntent,
                0);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notification = new Notification.Builder(this, createChannel())
                    .setSmallIcon(R.drawable.navisens_logo_big)
                    .setTicker("Navisens")
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("Navisens")
                    .setContentText("MotionDna is running")
                    .setContentIntent(contentIntent)
                    .setOngoing(true)
                    .build();
        } else {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.navisens_logo_big)
                    .setTicker("Navisens")
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("Navisens")
                    .setContentText("MotionDna is running")
                    .setContentIntent(contentIntent)
                    .setOngoing(true)
                    .build();
        }

        startForeground(NOTIFICATION_ID, notification);
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "Navisens Location";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("Navisens Channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "Navisens Channel";
    }

    //takes single client
    public class LocalMotionDnaServiceBinder extends Binder {
        MotionDnaForegroundService getService() {
            return MotionDnaForegroundService.this;
        }
    }

    final IBinder motionDnaServiceBinder = new LocalMotionDnaServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return motionDnaServiceBinder;
    }

    @Override
    public void onDestroy() {

        stopForeground(true);
        stopSelf();

        super.onDestroy();
    }
}
