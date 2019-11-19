package com.chzu.ice.schat.activities.chat;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.chzu.ice.schat.R;
import com.chzu.ice.schat.utils.RSAUtil;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_act);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        ChatActivity t = this;

        Log.i(TAG, "onCreate: "+RSAUtil.byte2Base64(RSAUtil.base642Byte("哈多么122")));
        de.hdodenhof.circleimageview.CircleImageView friendAvatar = findViewById(R.id.friendAvatarIM);
        new Handler().post(() -> Glide.with(t).load("https://img2.woyaogexing.com/2019/11/16/d1f0f2a6e50245eaa26fbe47e9130638!400x400.jpeg").into(friendAvatar));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
