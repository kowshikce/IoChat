package com.example.iochat.ForegroundServices;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.iochat.MainActivity;
import com.example.iochat.Notifications.NotificationConfigObject;
import com.example.iochat.Notifications.SimpleNotification;
import com.example.iochat.R;
import com.example.iochat.Registry.ActivitymainRegistry;
import com.example.iochat.Registry.RegistryMainConstant;
import com.example.iochat.Utils.HandlerKeeper;

public class DedicatedThreadService extends Service{

    Handler mainHandler;
    public Looper serviceLooper;
    public ServiceHandler serviceHandler;
    private ActivitymainRegistry registry;
    private static final String TAG = "DedicatedThreadService";
    public static final String CHANNEL_ID = "simpleChannel";
    public static final int NOTIFICATION_ID = 6;
    private final class ServiceHandler extends Handler{


        public ServiceHandler(Looper looper){
            super(looper);
            registry = ActivitymainRegistry.getInstance();
            mainHandler =(Handler)registry.getRegistryFor(RegistryMainConstant.MAIN_HANDLER_KEY);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Message message = mainHandler.obtainMessage();
            message.arg1 = 1;
            message.sendToTarget();

            try{
                Thread.sleep(5000);
            }catch (InterruptedException e){
                Log.i(TAG, "Error", e);
                Thread.currentThread().interrupt();
            }
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartHandler", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
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
            if(notification != null) startForeground(NOTIFICATION_ID, notification);
            Log.i(TAG,"Notification: " +  String.valueOf(notification == null));
        }catch (Exception e){
            Log.i(TAG, "Error", e);
        }
        Log.i(TAG, "onStartCommand");

        Message message = serviceHandler.obtainMessage();
        message.arg1 = startId;
        serviceHandler.sendMessage(message);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Message msg = mainHandler.obtainMessage();
        msg.arg1 = 0;
        msg.sendToTarget();
    }
}
