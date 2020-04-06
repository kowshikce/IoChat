package com.example.iochat.ForegroundServices;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.iochat.MainActivity;
import com.example.iochat.Notifications.NotificationConfigObject;
import com.example.iochat.Notifications.SimpleNotification;
import com.example.iochat.R;

public class ExampleService extends Service{

    public static final String CHANNEL_ID = "simpeChannel";
    private static final String TAG = "ExampleService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String text = intent.getStringExtra(ServiceConstants.textKey);
        NotificationConfigObject config = new NotificationConfigObject(R.drawable.ic_launcher_foreground,
                "A Foreground Service", text);

        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent p_intent = PendingIntent.getActivity(getApplicationContext(), 0, intent1,0 );
        try{
            Notification notification = SimpleNotification.showServiceNotification(this, CHANNEL_ID, config, p_intent);
            if(notification != null) startForeground(3, notification);
            Log.i(TAG,"Notification: " +  String.valueOf(notification == null));
        }catch (Exception e){
            Log.i(TAG, "Error", e);
        }
        Log.i(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
