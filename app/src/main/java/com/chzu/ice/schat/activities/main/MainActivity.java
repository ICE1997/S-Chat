package com.chzu.ice.schat.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.main.chatsList.ChatsListFragment;
import com.chzu.ice.schat.activities.main.friendsList.FriendsListFragment;
import com.chzu.ice.schat.activities.main.settingsList.SettingsListFragment;
import com.chzu.ice.schat.utils.MQTTController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView editStateChangeBtn;
    private ViewGroup bottomNav;
    private TextView mainToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.main_act);
        registerComponents();
        registerListeners();
        ((BottomNavigationView) bottomNav).setSelectedItemId(R.id.item_chats);


        ArrayList<String> topics = new ArrayList<>();
        topics.add(App.getSignedInUserTopic());
        MQTTController.registerConnectSucceedBRReceiver(topics);
        MQTTController.sendSubscribeBroadCast(topics);
        MQTTController.sendConnectBroadcast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MQTTController.sendConnectBroadcast();
        Log.d(TAG, "onResume: ");
    }

    private void registerComponents() {
        editStateChangeBtn = findViewById(R.id.editStateChangeBtn);
        bottomNav = findViewById(R.id.bottomNav);
        mainToolbarTitle = findViewById(R.id.mainToolbarTittle);
    }

    private void registerListeners() {
        ((BottomNavigationView) bottomNav).setOnNavigationItemSelectedListener(new OnChangeFragmentListener());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private class OnChangeFragmentListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        private final FragmentManager fragmentManager = getSupportFragmentManager();
        private final Fragment chatFrag = ChatsListFragment.newInstance();
        private final Fragment friendFrag = FriendsListFragment.newInstance();
        private final Fragment settingFrag = SettingsListFragment.newInstance();

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_chats:
                    changeFrag(1, chatFrag, "Chats");
                    return true;
                case R.id.item_friends:
                    changeFrag(2, friendFrag, "Friends");
                    return true;
                case R.id.item_settings:
                    changeFrag(3, settingFrag, "Settings");
                    return true;
            }
            return false;
        }

        private void changeFrag(int i, Fragment newFrag, String title) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.mainContent, newFrag);
            transaction.commit();
            if (i == 1) {
                editStateChangeBtn.setVisibility(View.VISIBLE);
            } else {
                editStateChangeBtn.setVisibility(View.GONE);
            }
            mainToolbarTitle.setText(title);
        }
    }
}
