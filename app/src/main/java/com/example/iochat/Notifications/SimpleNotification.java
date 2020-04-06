package com.example.iochat.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.iochat.R;

public class SimpleNotification {


    public static void showNotification(@NonNull Context context, @NonNull final String CHANNEL_ID,@NonNull NotificationConfigObject config) throws NullPointerException{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if(notificationManager != null) notificationManager.createNotificationChannel(channel);
            if(notificationManager == null) throw new NullPointerException("Notification Manager is Null.");
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(config.getIcon())
                .setContentTitle(config.getTitle())
                .setContentText(config.getText())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }

    public static void showProgressNotification(@NonNull Context context, @NonNull final String CHANNEL_ID, @NonNull NotificationConfigObject config) throws NullPointerException, InterruptedException {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if(notificationManager != null) notificationManager.createNotificationChannel(channel);
            if(notificationManager == null) throw new NullPointerException("Notification Manager is Null.");
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(config.getIcon())
                .setContentTitle(config.getTitle())
                .setContentText(config.getText())
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        managerCompat.notify(2, builder.build());


        Thread x = new Thread(() -> {
            for(int i = 0; i <= 100; i++){
                builder.setProgress(PROGRESS_MAX, i, false).setContentText("Download in Progress "+i+"%");
                managerCompat.notify(2, builder.build());
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            builder.setContentText("Completed").setProgress(0, 0, false);
            managerCompat.notify(2, builder.build());
        });

        x.start();
    }

    public static Notification showServiceNotification(@NonNull Context context, @NonNull final String CHANNEL_ID, @NonNull NotificationConfigObject config, PendingIntent intent) throws NullPointerException{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if(notificationManager != null) notificationManager.createNotificationChannel(channel);
            if(notificationManager == null) throw new NullPointerException("Notification Manager is Null.");
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(config.getIcon())
                .setContentTitle(config.getTitle())
                .setContentText(config.getText())
                .setContentIntent(intent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    public static void showExpendableNotification(Context context, final String CHANNEL_ID, NotificationConfigObject config){

    }
}
