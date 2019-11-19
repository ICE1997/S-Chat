package com.chzu.ice.schat.activities.main.friendsList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.schat.R;
import com.chzu.ice.schat.adpters.FriendsRCVAdapter;

public class FriendsListFragment extends Fragment {
    private static final String TAG = FriendsListFragment.class.getSimpleName();
    private RecyclerView friendListRV;

    public static FriendsListFragment newInstance() {
        return new FriendsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag_friendslist, container, false);

        friendListRV = root.findViewById(R.id.friendListRV);
        friendListInit();
        return root;
    }


    private void friendListInit() {
        FriendsRCVAdapter adapter = new FriendsRCVAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setItemPrefetchEnabled(true);
        linearLayoutManager.setInitialPrefetchItemCount(10);
        friendListRV.setHasFixedSize(true);
        friendListRV.setLayoutManager(linearLayoutManager);
        friendListRV.setAdapter(adapter);
        friendListRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + v.getId());
            }
        });
    }
}
