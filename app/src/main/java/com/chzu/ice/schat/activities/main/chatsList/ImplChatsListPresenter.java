package com.chzu.ice.schat.activities.main.chatsList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.adpters.ChatListItem;
import com.chzu.ice.schat.configs.MQTTConfig;
import com.chzu.ice.schat.data.LocalRepository;
import com.chzu.ice.schat.pojos.database.ChatListE;
import com.chzu.ice.schat.pojos.mqtt.Message;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ImplChatsListPresenter implements ChatsListContract.Presenter {
    private ChatsListContract.View view;

    ImplChatsListPresenter(ChatsListContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadAllChatList() {
        List<ChatListE> chatListEList = LocalRepository.localGetAllChatListByUsername(App.getSignedInUsername());
        final Calendar calendar = Calendar.getInstance();
        List<ChatListItem> items = new ArrayList<>();
        for (ChatListE chatListE : chatListEList) {
            calendar.setTimeInMillis(chatListE.getLatestChatTime());
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            ChatListItem item = new ChatListItem(chatListE.getUnReadMessageNum(), chatListE.getFriendName(), chatListE.getLatestMsg(), h + ":" + (m > 9 ? m : 0 + String.valueOf(m)));
            items.add(item);
        }
        view.loadAllChatListComplete(items);
    }

    public void read(String friendName) {
        new Thread(() -> LocalRepository.localSetChatReadByUsernameAndFriendName(App.getSignedInUsername(), friendName)).start();
    }

    public void delete(String friendName) {
        new Thread(() -> LocalRepository.deleteChatlistByUsernameAndFriendName(App.getSignedInUsername(), friendName)).start();
    }

    @Override
    public void registerReceiverMessageListener(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_RECEIVE_MESSAGE);
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Gson gson = new Gson();
                Message message = gson.fromJson(intent.getStringExtra(MQTTConfig.EXTRA_RECEIVE_MESSAGE_MESSAGE), Message.class);
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(message.getSendTime());
                int h = calendar.get(Calendar.HOUR_OF_DAY);
                int m = calendar.get(Calendar.MINUTE);
                ChatListE chatListE = LocalRepository.localGetChatListByUsernameAndFriendName(App.getSignedInUsername(), message.getSender());
                ChatListItem chatListItem;
                if (chatListE != null) {
                    chatListItem = new ChatListItem(chatListE.getUnReadMessageNum(), message.getSender(), message.getMsg(), h + ":" + (m > 9 ? m : 0 + String.valueOf(m)));
                    view.updateOrInsertChat(chatListItem);
                }
            }
        }, intentFilter);
    }
}
