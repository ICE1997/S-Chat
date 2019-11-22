package com.chzu.ice.schat.activities.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.auth.AuthActivity;
import com.chzu.ice.schat.activities.main.MainActivity;
import com.chzu.ice.schat.utils.UIManager;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIManager.setFullScreen(getWindow());
        setContentView(R.layout.splash_act);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!App.getHasSignedIn()) {
                Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                startActivity(intent);
            }
            this.finish();
        }).start();
    }
}
