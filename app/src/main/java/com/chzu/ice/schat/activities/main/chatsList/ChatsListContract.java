package com.chzu.ice.schat.activities.main.chatsList;

import android.content.Context;

import com.chzu.ice.schat.activities.BasePresenter;
import com.chzu.ice.schat.activities.BaseView;
import com.chzu.ice.schat.adpters.ChatListItem;

import java.util.List;

public interface ChatsListContract {
    interface View extends BaseView<Presenter> {
        void loadAllChatListComplete(List<ChatListItem> chatListItemList);

        void updateOrInsertChat(ChatListItem item);
    }

    interface Presenter extends BasePresenter {
        void loadAllChatList();

        void read(String friendName);

        void delete(String friendName);

        void registerReceiverMessageListener(Context context);
    }
}
