package com.chzu.ice.schat.activities.main.friendsList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chzu.ice.schat.R;

public class FriendsListFragment extends Fragment {

    public static FriendsListFragment newInstance() {
        return new FriendsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag_friendslist, container, false);
        return root;
    }
}
