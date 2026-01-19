package com.vinaekal.sisehat.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SessionService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // Method ini dipanggil saat aplikasi di-swipe dari recent apps (Kill Task)
        Session session = new Session(this);
        session.clear(); // Hapus session login
        stopSelf();
    }
}