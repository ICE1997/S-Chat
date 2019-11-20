package com.chzu.ice.schat.activities.chat;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.chzu.ice.schat.R;
import com.chzu.ice.schat.adpters.ChatItem;
import com.chzu.ice.schat.adpters.ChatRCVAdapter;
import com.chzu.ice.schat.views.custom.CustomLinearLayoutManager;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements ChatContract.View {
    public static final String EXTRA_USERNAME = "username";

    private static final String TAG = ChatActivity.class.getSimpleName();
    ChatRCVAdapter chatRCVAdapter;
    private ChatContract.Presenter presenter;
    private CircleImageView friendAvatar;
    private RecyclerView chatsRCV;
    private ImageButton sendMsgImgBtn;
    private EditText msgEDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_act);
        new ImplChatPresenter(this);
        friendAvatar = findViewById(R.id.friendAvatarIM);
        chatsRCV = findViewById(R.id.chatsRCV);
        sendMsgImgBtn = findViewById(R.id.sendMsgImgBtn);
        msgEDT = findViewById(R.id.msgEDT);

        sendMsgImgBtn.setOnClickListener((v) -> {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int h = calendar.get(Calendar.HOUR);
            int m = calendar.get(Calendar.MINUTE);
            ChatItem chatItem = new ChatItem(msgEDT.getText().toString(), h + ":" + (m > 9 ? m : 0 + String.valueOf(m)), true);
            chatRCVAdapter.addChatItem(chatItem);
            msgEDT.setText("");
            chatsRCV.smoothScrollToPosition(chatRCVAdapter.getItemCount() - 1);
        });

        CustomLinearLayoutManager llm = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        llm.setItemPrefetchEnabled(true);
        llm.setInitialPrefetchItemCount(5);

        chatRCVAdapter = new ChatRCVAdapter();

        chatsRCV.setHasFixedSize(true);
        ((SimpleItemAnimator) chatsRCV.getItemAnimator()).setSupportsChangeAnimations(false);
        chatsRCV.getItemAnimator().setAddDuration(0);
        chatsRCV.setAdapter(chatRCVAdapter);
        chatsRCV.setLayoutManager(llm);
        chatsRCV.scrollToPosition(chatRCVAdapter.getItemCount() - 1);
        initUI();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void initUI() {
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

        new Handler(getMainLooper()).post(() -> Glide.with(getApplicationContext()).load("https://img2.woyaogexing.com/2019/11/16/d1f0f2a6e50245eaa26fbe47e9130638!400x400.jpeg").into(friendAvatar));
    }
}
