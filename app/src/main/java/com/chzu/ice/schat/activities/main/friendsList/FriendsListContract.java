package com.chzu.ice.schat.activities.main.friendsList;

import com.chzu.ice.schat.activities.BasePresenter;
import com.chzu.ice.schat.activities.BaseView;
import com.chzu.ice.schat.adpters.FriendListItem;
import com.chzu.ice.schat.pojos.database.FriendE;

import java.util.ArrayList;

public interface FriendsListContract {
    interface View extends BaseView<Presenter> {
        void afterLoad();

        void endLoad(ArrayList<FriendListItem> friendListItems);
    }

    interface Presenter extends BasePresenter {
        void loadAllFriendsByUsername(String username);
    }
}