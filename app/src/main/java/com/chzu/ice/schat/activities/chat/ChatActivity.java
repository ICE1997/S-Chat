package com.chzu.ice.schat.activities.chat;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.chzu.ice.schat.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_act);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        ChatActivity t = this;

        de.hdodenhof.circleimageview.CircleImageView friendAvatar = findViewById(R.id.friendAvatarIM);
        new Handler().post(() -> Glide.with(t).load("https://img2.woyaogexing.com/2019/11/16/d1f0f2a6e50245eaa26fbe47e9130638!400x400.jpeg").into(friendAvatar));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
