package com.arattan.android.blloctest_app1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

class NotificationListenerService extends android.service.notification.NotificationListenerService {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    public static StatusBarNotification mPostedNotification;
    Context context = getApplicationContext();
    static final String LOG_TAG = "NotificationService";

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            updateCurrentNotification();
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Assign HandlerThread's Looper to Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

        Log.d(LOG_TAG, "Service been been created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        onListenerConnected();
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // If killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        mPostedNotification = sbn;

        String pack = sbn.getPackageName();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();
        String id = sbn.getTag();
    }

    private void updateCurrentNotification() {
        StatusBarNotification[] activeNotifications = getActiveNotifications();
    }
}
