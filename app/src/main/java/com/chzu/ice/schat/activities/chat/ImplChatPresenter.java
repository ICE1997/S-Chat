package com.chzu.ice.schat.activities.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.adpters.ChatItem;
import com.chzu.ice.schat.configs.MQTTConfig;
import com.chzu.ice.schat.data.LocalRepository;
import com.chzu.ice.schat.pojos.database.FriendE;
import com.chzu.ice.schat.pojos.database.MessageE;
import com.chzu.ice.schat.pojos.mqtt.Message;
import com.chzu.ice.schat.utils.MQTTController;
import com.chzu.ice.schat.utils.MessageProcessor;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ImplChatPresenter implements ChatContract.Presenter {
    private static final String TAG = ImplChatPresenter.class.getSimpleName();
    private ChatContract.View view;

    ImplChatPresenter(ChatContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void sendMessageTo(String content, String friendName) {
        view.afterSendMessage();
        new Thread(() -> {
            FriendE friendE = LocalRepository.localGetFriendByUsernameAndFriendName(App.getSignedInUsername(), friendName);
            try {
                Message message = new Message();
                message.setMsg(content);
                message.setSendTime(new Date().getTime());
                message.setSender(App.getSignedInUsername());
                message.setReceiver(friendName);
                String res = MessageProcessor.encryptMessage(message, friendE);
                MQTTController.sendSendMessageBroadCast(res, friendE.getFriendTopic());

                MessageE messageE = new MessageE();
                messageE.setContent(message.getMsg());
                messageE.setSender(message.getSender());
                messageE.setReceiver(message.getReceiver());
                messageE.setUserSend(true);
                messageE.setTimestamp(message.getSendTime());

                LocalRepository.localAddMessage(messageE);


                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                int h = calendar.get(Calendar.HOUR_OF_DAY);
                int m = calendar.get(Calendar.MINUTE);
                ChatItem chatItem = new ChatItem();
                chatItem.setTime(h + ":" + (m > 9 ? m : 0 + String.valueOf(m)));
                chatItem.setIsSender(true);
                chatItem.setContent(content);

                view.endSendMessage(chatItem);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
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
                ChatItem chatItem = new ChatItem();
                chatItem.setTime(h + ":" + (m > 9 ? m : 0 + String.valueOf(m)));
                chatItem.setIsSender(false);
                chatItem.setContent(message.getMsg());

                view.afterReceiverMessage(chatItem);
            }
        }, intentFilter);
    }

    @Override
    public void loadAllMessagesByFriendName(String friendName) {
        List<MessageE> messageES = LocalRepository.localGetAllMessagesByUsernameAndFriendName(App.getSignedInUsername(), friendName);
        List<ChatItem> chatItems = new ArrayList<>();
        final Calendar calendar = Calendar.getInstance();

        for (MessageE messageE : messageES) {
            calendar.setTimeInMillis(messageE.getTimestamp());
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            ChatItem chatItem = new ChatItem();
            chatItem.setTime(h + ":" + (m > 9 ? m : 0 + String.valueOf(m)));
            chatItem.setContent(messageE.getContent());
            if (messageE.isUserSend()) {
                chatItem.setIsSender(true);
            } else {
                chatItem.setIsSender(false);
            }
            chatItems.add(chatItem);
        }
        view.loadAllMessageCompleted(chatItems);
    }
}
