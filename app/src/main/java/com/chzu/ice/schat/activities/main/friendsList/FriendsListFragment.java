package com.chzu.ice.schat.activities.main.friendsList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.chat.ChatActivity;
import com.chzu.ice.schat.adpters.FriendListItem;
import com.chzu.ice.schat.adpters.FriendListRCVAdapter;

import java.util.ArrayList;

public class FriendsListFragment extends Fragment implements FriendsListContract.View {
    private static final String TAG = FriendsListFragment.class.getSimpleName();
    private static FriendListRCVAdapter adapter = new FriendListRCVAdapter();
    private RecyclerView friendListRV;
    private TextView title;
    private FriendsListContract.Presenter presenter;

    public static FriendsListFragment newInstance() {
        return new FriendsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag_friendslist, container, false);
        new ImplFriendsListPresenter(this);
        friendListRV = root.findViewById(R.id.friendListRV);
        friendListInit();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title = getActivity().findViewById(R.id.mainToolbarTittle);
        presenter.loadAllFriendsByUsername(App.getSignedInUsername());
    }

    private void friendListInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setItemPrefetchEnabled(true);
        linearLayoutManager.setInitialPrefetchItemCount(10);
        friendListRV.setHasFixedSize(true);
        friendListRV.setLayoutManager(linearLayoutManager);
        friendListRV.setAdapter(adapter);
        adapter.setOnClickListener((name) -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void setPresenter(FriendsListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void afterLoad() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (title != null) {
                title.setText("Refreshing...");
            } else {
                Log.e(TAG, "afterLoad: Title为空");
            }
            Log.i(TAG, "afterLoad: 拉取数据中");
        });
    }

    @Override
    public void endLoad(ArrayList<FriendListItem> friendListItems) {
        Log.i(TAG, "endLoad: 拉去数据结束");
        new Handler(Looper.getMainLooper()).post(() -> {
            adapter.setDataSet(friendListItems);
            if (title != null) {
                title.setText("Friends");
            }
        });
    }
}
