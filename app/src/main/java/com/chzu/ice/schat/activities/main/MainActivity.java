package com.chzu.ice.schat.activities.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.main.chatsList.ChatsListFragment;
import com.chzu.ice.schat.activities.main.friendsList.FriendsListFragment;
import com.chzu.ice.schat.activities.main.settingsList.SettingsListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView editStateChangeBtn;
    private ViewGroup bottomNav;
    private TextView mainToolbarTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.main_act);
        registerComponents();
        registerListeners();
        ((BottomNavigationView) bottomNav).setSelectedItemId(R.id.item_chats);
    }

    private void registerComponents() {
        editStateChangeBtn = findViewById(R.id.editStateChangeBtn);
        bottomNav = findViewById(R.id.bottomNav);
        mainToolbarTittle = findViewById(R.id.mainToolbarTittle);
    }

    private void registerListeners() {
        ((BottomNavigationView) bottomNav).setOnNavigationItemSelectedListener(new OnChangeFragmentListener());
    }

    private class OnChangeFragmentListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        private final ArrayList<Integer> added = new ArrayList<>();
        private final FragmentManager fragmentManager = getSupportFragmentManager();
        private final Fragment chatFrag = ChatsListFragment.newInstance();
        private final Fragment friendFrag = FriendsListFragment.newInstance();
        private final Fragment settingFrag = SettingsListFragment.newInstance();

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_chats:
                    changeFrag(settingFrag, friendFrag, 1, chatFrag, "Chats");
                    return true;
                case R.id.item_friends:
                    changeFrag(settingFrag, chatFrag, 2, friendFrag, "Friends");
                    return true;
                case R.id.item_settings:
                    changeFrag(friendFrag, chatFrag, 3, settingFrag, "Settings");
                    return true;
            }
            return false;
        }

        private void changeFrag(Fragment oldFrag1, Fragment oldFrag2, int i, Fragment newFrag, String tabName) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(oldFrag2);
            transaction.hide(oldFrag1);
            if (!added.contains(i)) {
                transaction.add(R.id.mainContent, newFrag);
                added.add(i);
            }
            transaction.show(newFrag);
            transaction.commit();
            editStateChangeBtn.setVisibility(View.GONE);
            mainToolbarTittle.setText(tabName);
        }

    }
}
