package com.chzu.ice.schat.activities.main.settingsList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.chzu.ice.schat.App;
import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.auth.AuthActivity;
import com.chzu.ice.schat.utils.MQTTController;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsListFragment extends Fragment {
    private CircleImageView avatarIM;
    private TextView nameTV;
    private Button logoutBtn;

    public static SettingsListFragment newInstance() {
        return new SettingsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag_settingslist, container, false);
        avatarIM = root.findViewById(R.id.avatarIM);
        Glide.with(root).load("https://img2.woyaogexing.com/2019/11/16/d1f0f2a6e50245eaa26fbe47e9130638!400x400.jpeg").into(avatarIM);

        nameTV = root.findViewById(R.id.nameTV);
        nameTV.setText(App.getSignedInUsername());

        logoutBtn = root.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setPositiveButton("Yes", (dialog, which) -> {
                ArrayList<String> topics = new ArrayList<>();
                topics.add(App.getSignedInUserTopic());

                App.logOut();
                MQTTController.sendUnSubscribeBroadCast(topics);
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
                getActivity().finish();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setMessage("Sure?");
            builder.show();
        });
        return root;
    }
}
