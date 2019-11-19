package com.chzu.ice.schat.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastHelper {
    public static void showToast(final Context ctx, final String msg, final int duration) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(ctx, msg, duration).show());
    }
}
