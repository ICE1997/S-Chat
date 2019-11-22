package com.chzu.ice.schat.activities.main.friendsList;

import android.util.Log;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.activities.auth.register.ImplRegisterPresenter;
import com.chzu.ice.schat.adpters.FriendListItem;
import com.chzu.ice.schat.data.LocalRepository;
import com.chzu.ice.schat.data.RemoteRepository;
import com.chzu.ice.schat.pojos.database.AccountE;
import com.chzu.ice.schat.pojos.database.FriendE;
import com.chzu.ice.schat.pojos.gson.resp.BaseResponse;
import com.chzu.ice.schat.pojos.gson.resp.data.LoadAllFriendRelationsData;

import java.util.ArrayList;
import java.util.List;

public class ImplFriendsListPresenter implements FriendsListContract.Presenter {
    private static final String TAG = ImplRegisterPresenter.class.getSimpleName();
    private FriendsListContract.View view;

    ImplFriendsListPresenter(FriendsListContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadAllFriendsByUsername(String username) {
        view.afterLoad();
        new Thread(() -> {
            ArrayList<FriendListItem> friendListItems = new ArrayList<>();
            AccountE accountE = LocalRepository.localGetAccountByUsername(username);
            if (accountE != null) {
                BaseResponse<List<LoadAllFriendRelationsData>> relations = RemoteRepository.remoteLoadAllFriendsByToken(App.getSignedInUserAccessToken());
                if (relations != null) {
                    switch (relations.code) {
                        case "10401":
                            Log.i(TAG, "loadAllFriendsByUsername: 拉取数据成功");
                            for (LoadAllFriendRelationsData d :
                                    relations.data) {
                                FriendE friendE = new FriendE();
                                friendE.setPublicKey(d.friendPublicKey);
                                friendE.setFriendTopic(d.friendTopic);
                                friendE.setFriendName(d.friendName);
                                friendE.setUsername(d.userName);
                                LocalRepository.localAddFriend(friendE);
                                friendListItems.add(new FriendListItem("", d.friendName));
                            }
                            break;
                        case "10402":
                            break;
                        case "401":
                            Log.i(TAG, "loadAllFriendsByUsername: AccessToken失效，请获取新的token" + App.getSignedInUserAccessToken());
                            BaseResponse baseResponse = RemoteRepository.remoteGetAccessTokenByRefreshToken(App.getSignedInUserRefreshToken());
                            if (baseResponse != null) {
                                if ("10501".equals(baseResponse.code)) {
                                    accountE.setAccessToken((String) baseResponse.data);
                                    App.addUser(accountE);
                                    loadAllFriendsByUsername(username);
                                } else {
                                    Log.e(TAG, "loadAllFriendsByUsername: RefreshToken过期，需要重新登录" + App.getSignedInUserRefreshToken());
                                }
                            }
                            break;
                        default:
                    }
                    view.endLoad(friendListItems);
                } else {
                    Log.e(TAG, "run: 数据解析失败");
                }
            } else {
                Log.e(TAG, "loadAllFriendsByUsername: 无此账户");
            }
        }).start();
    }
}
