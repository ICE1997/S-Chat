package com.chzu.ice.schat.activities.chat;

import android.content.Context;

import com.chzu.ice.schat.activities.BasePresenter;
import com.chzu.ice.schat.activities.BaseView;
import com.chzu.ice.schat.adpters.ChatItem;

import java.util.List;

public interface ChatContract {
    interface View extends BaseView<Presenter> {
        void afterSendMessage();

        void endSendMessage(ChatItem chatItem);

        void showSendMessageSucceed();

        void showSendMessageFailed();

        void loadAllMessageCompleted(List<ChatItem> chatItems);

        void afterReceiverMessage(ChatItem chatItem);
    }

    interface Presenter extends BasePresenter {
        void sendMessageTo(String content, String friendName);

        void registerReceiverMessageListener(Context context);

        void loadAllMessagesByFriendName(String friendName);
    }
}
