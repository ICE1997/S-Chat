package com.chzu.ice.schat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.chzu.ice.schat.utils.MQTTController;

public class MQTTService extends Service {
    private final String TAG = MQTTService.class.getSimpleName();

    public MQTTService() {
        new MQTTController();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
