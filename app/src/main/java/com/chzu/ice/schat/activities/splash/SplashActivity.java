package com.chzu.ice.schat.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.authentication.AuthActivity;
import com.chzu.ice.schat.utils.UIManager;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIManager.setFullScreen(getWindow());
        setContentView(R.layout.splash_act);
        Intent intent = new Intent(this, AuthActivity.class);
        new Handler().postDelayed(() -> {
            startActivity(intent);
            this.finish();
        }, 2000);
    }

}
