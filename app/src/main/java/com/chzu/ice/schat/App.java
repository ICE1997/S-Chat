package com.chzu.ice.schat;

import android.app.Application;

import com.chzu.ice.schat.utils.ObjectBoxHelper;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBoxHelper.init(this);
        ObjectBoxHelper.startDebug(true, this);
    }
}
