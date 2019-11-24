package com.chzu.ice.schat.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.chat.ChatActivity;
import com.chzu.ice.schat.configs.MQTTConfig;
import com.chzu.ice.schat.data.LocalRepository;
import com.chzu.ice.schat.pojos.database.ChatListE;
import com.chzu.ice.schat.pojos.database.MessageE;
import com.chzu.ice.schat.pojos.mqtt.Message;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MQTTController {
    private static final String TAG = MQTTController.class.getSimpleName();

    private MqttAsyncClient mClient;
    private MqttConnectOptions opt;
    private IMqttActionListener connectListener;
    private IMqttActionListener subscribeListener;
    private IMqttActionListener publishListener;

    public MQTTController() {
        try {
            init();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void sendConnectBroadcast() {
        Intent intent = new Intent(MQTTConfig.SIGNAL_CONNECT);
        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
    }

    public static void sendSubscribeBroadCast(ArrayList<String> topics) {
        Log.d(TAG, "sendSubscribeBroadCast: " + Arrays.toString(topics.toArray()));
        Intent intent = new Intent(MQTTConfig.SIGNAL_SUBSCRIBE);
        intent.putStringArrayListExtra(MQTTConfig.EXTRA_SUBSCRIBE_TOPIC, topics);
        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
    }

    public static void sendUnSubscribeBroadCast(ArrayList<String> topics) {
        Log.d(TAG, "sendSubscribeBroadCast: " + Arrays.toString(topics.toArray()));
        Intent intent = new Intent(MQTTConfig.SIGNAL_UNSUBSCRIBE);
        intent.putStringArrayListExtra(MQTTConfig.EXTRA_SUBSCRIBE_TOPIC, topics);
        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
    }

    public static void sendSendMessageBroadCast(String content, String topic) {
        Intent intent = new Intent(MQTTConfig.SIGNAL_SEND_MESSAGE);
        intent.putExtra(MQTTConfig.EXTRA_SEND_MESSAGE_MESSAGE, content);
        intent.putExtra(MQTTConfig.EXTRA_SEND_MESSAGE_TOPIC, topic);

        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
    }

    /***
     *处理连接返回结果
     */
    public static void registerConnectSucceedBRReceiver(ArrayList<String> topics) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_CONNECT_SUCCEED);
        LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(new BroadcastReceiver() {
            public String TAG = "TEst";

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: 连接成功");
                sendSubscribeBroadCast(topics);
            }
        }, intentFilter);
    }

    private void connect() throws MqttException {
        if (!mClient.isConnected()) {
            mClient.connect(opt, connectListener);
        } else {
            Log.d(TAG, "connect: 已经连接");
        }
    }

    private void disConnect() throws MqttException {
        if (mClient.isConnected()) {
            mClient.disconnect();
        } else {
            Log.d(TAG, "disConnect: 已经断连");
        }
    }

    private void unsubscribe(ArrayList<String> topics) throws MqttException {
        if (mClient.isConnected()) {
            Log.d(TAG, "unSubscribe: " + topics);
            String[] ts = new String[topics.size()];
            for (int i = 0; i < topics.size(); i++) {
                ts[i] = topics.get(i);
            }
            mClient.unsubscribe(ts);
            Log.d(TAG, "unsubscribe: 取消订阅成功");
        } else {
            Log.d(TAG, "publish: 客户端未连接");
        }
    }

    /**
     * 发送MQTT客户端连接结果广播
     * 包括失败，成功
     */
    private void sendConnectSucceedBroadcast() {
        Intent intent = new Intent(MQTTConfig.SIGNAL_CONNECT_SUCCEED);
        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
    }

    /**
     * 发送收到信息的广播
     * 应用内通信，用于收到新的信息后，发送广播来通知应用收到新的信息
     */
    private void sendNewMessageByGsonBroadcast(String msgGson) {
        Intent intent = new Intent(MQTTConfig.SIGNAL_RECEIVE_MESSAGE);
        intent.putExtra(MQTTConfig.EXTRA_RECEIVE_MESSAGE_MESSAGE, msgGson);
        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
    }

    /**
     * 发送信息成功的广播
     * 应用内通信，用于发送信息发送成功的广播（但是不能说明对方已经收到信息）
     */
    private void sendSendMessageSucceedBroadcast() {
        Intent intent = new Intent(MQTTConfig.SIGNAL_SEND_MESSAGE_SUCCEED);
        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
    }

    /**
     * 注册连接MQTT的广播接收器
     * 应用内通信，一般用于在登录成功/到主页面后，发送此类广播
     * 一旦连接，且处于连接状态，那么则不能在连接
     */
    private void registerConnectSignalReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_CONNECT);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!mClient.isConnected()) {
                    try {
                        connect();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onReceive: 连接...");
                } else {
                    Log.d(TAG, "onReceive: 已经连接");
                }
            }
        }, intentFilter);
    }


    /**
     * 注册订阅主题的广播接收器
     * 应用内通信，一般在获取到所有订阅主题的时候发送此类广播
     */
    private void registerSubscribeTopicSignalReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_SUBSCRIBE);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<String> topics = intent.getStringArrayListExtra(MQTTConfig.EXTRA_SUBSCRIBE_TOPIC);
                System.out.println(Arrays.toString(new ArrayList[]{topics}));
                try {
                    subscribe(topics);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, intentFilter);
    }

    /**
     * 取消注册订阅主题的广播接收器
     * 应用内通信，一般在获取到所有订阅主题的时候发送此类广播
     */
    private void registerUnSubscribeTopicSignalReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_UNSUBSCRIBE);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<String> topics = intent.getStringArrayListExtra(MQTTConfig.EXTRA_SUBSCRIBE_TOPIC);
                System.out.println(Arrays.toString(new ArrayList[]{topics}));
                try {
                    unsubscribe(topics);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, intentFilter);
    }

    /**
     * 注册发送信息的广播接收器
     * 应用内通信，一般在聊天界面发送此类广播
     */
    private void registerSendMessageSignalReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_SEND_MESSAGE);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String t = intent.getStringExtra(MQTTConfig.EXTRA_SEND_MESSAGE_TOPIC);
                String m = intent.getStringExtra(MQTTConfig.EXTRA_SEND_MESSAGE_MESSAGE);
                Log.d(TAG, "onReceive: Message" + m);
                try {
                    publish(t, m);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, intentFilter);
    }

    private void subscribe(ArrayList<String> topics) throws MqttException {
        if (mClient.isConnected()) {
            Log.d(TAG, "subscribe: " + topics);
            String[] ts = new String[topics.size()];
            for (int i = 0; i < topics.size(); i++) {
                ts[i] = topics.get(i);
            }
            mClient.subscribe(ts, new int[]{MQTTConfig.QOS}, this, subscribeListener);
        } else {
            Log.d(TAG, "publish: 客户端未连接");
        }
    }

    private void publish(final String topic, final String msg) throws MqttException {
        if (mClient.isConnected()) {
            mClient.publish(topic, msg.getBytes(), MQTTConfig.QOS, false, this, publishListener);
        } else {
            Log.d(TAG, "publish: 客户端未连接");
        }
    }

    private void init() throws MqttException {
        mClient = new MqttAsyncClient(MQTTConfig.MQTT_SERVER, MQTTConfig.CLIENT_ID, new MemoryPersistence());
        opt = new MqttConnectOptions();
        opt.setCleanSession(false);
        opt.setAutomaticReconnect(true);
        mClient.setCallback(new MMQTTCallback());
        connectListener = new ConnectListener();
        subscribeListener = new SubscribeListener();
        publishListener = new PublishListener();

        registerConnectSignalReceiver();
        registerSubscribeTopicSignalReceiver();
        registerSendMessageSignalReceiver();
        registerUnSubscribeTopicSignalReceiver();
    }

    private void processMessage(Message message) {

        Intent intent = new Intent(App.getContext(), ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_FRIEND_NAME, message.getSender());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(App.getContext());
        stackBuilder.addNextIntentWithParentStack(intent);

        Log.d(TAG, "processMessage: " + message.getSender());
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getContext(), "SChat Notification").
                setContentTitle(message.getSender())
                .setContentText(message.getMsg())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(App.getContext());
        notificationManagerCompat.notify(1, builder.build());


        MessageE messageE = new MessageE();
        messageE.setContent(message.getMsg());
        messageE.setSender(message.getSender());
        messageE.setReceiver(message.getReceiver());
        messageE.setTimestamp(message.getSendTime());
        LocalRepository.localAddMessage(messageE);


        ChatListE chatListEOld = LocalRepository.localGetChatListByUsernameAndFriendName(messageE.getReceiver(), messageE.getSender());

        if (chatListEOld != null) {
            chatListEOld.setUnReadMessageNum(chatListEOld.getUnReadMessageNum() + 1);
            chatListEOld.setLatestMsg(message.getMsg());
            chatListEOld.setUsername(App.getSignedInUsername());
            chatListEOld.setFriendName(messageE.getSender());
            chatListEOld.setLatestChatTime(messageE.getTimestamp());
            LocalRepository.localAddOrUpdateChatList(chatListEOld);
        } else {
            ChatListE chatListE = new ChatListE();
            chatListE.setUnReadMessageNum(1);
            chatListE.setLatestMsg(message.getMsg());
            chatListE.setUsername(App.getSignedInUsername());
            chatListE.setFriendName(messageE.getSender());
            chatListE.setLatestChatTime(messageE.getTimestamp());
            LocalRepository.localAddOrUpdateChatList(chatListE);
        }

        Gson gson = new Gson();
        sendNewMessageByGsonBroadcast(gson.toJson(message));
    }

    /**
     * 连接客户端的回调，包括连接成功和连接失败
     */
    private class ConnectListener implements IMqttActionListener {
        private final Timer timer = new Timer();
        private final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "onFailure: 连接失败，5s后重试");
                if (mClient.isConnected()) {
                    Log.d(TAG, "run: 重新连接成功");
                    this.cancel();
                }
            }
        };

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            sendConnectSucceedBroadcast();
            Log.d(TAG, "run: 连接成功");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            timer.schedule(task, 0, 5000);
        }
    }

    /**
     * 订阅主题监听器，监听订阅的结果，失败还是成功
     */
    private class SubscribeListener implements IMqttActionListener {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            for (String s :
                    asyncActionToken.getTopics()) {
                Log.d(TAG, "onSuccess: 订阅成功" + s);
            }
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(TAG, "onFailure: 订阅失败," + exception.getLocalizedMessage());
        }
    }

    private class PublishListener implements IMqttActionListener {

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(TAG, "onSuccess: 发布成功 -> " + Arrays.toString(asyncActionToken.getTopics()));

        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(TAG, "onFailure: 发布失败");
        }
    }

    /**
     * MQTT全程回调/监听器,用于实现MQTT的一些回调，比如当失联时，新信息到达时，或者信息发送成功时。
     */
    private class MMQTTCallback implements MqttCallback {
        private final Timer timer = new Timer();
        private final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "connectionLost: 连接丢失,重新连线");
                if (mClient.isConnected()) {
                    Log.d(TAG, "run: 已重连接.");
                    this.cancel();
                } else {
                    try {
                        connect();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        /**
         * 断连后，五秒一次，重新连接
         */
        @Override
        public void connectionLost(Throwable cause) {
            timer.schedule(task, 0, 5000);
        }

        /**
         * 新信息到达后，处理的事件
         * NOTE:这个topic是本机的topic,表示从所订阅的topic中来的信息。
         */
        @Override
        public void messageArrived(String topic, MqttMessage message) {
            Log.d(TAG, "messageArrived: 新消息");
            Log.d(TAG, "messageArrived: topic: to " + topic);
//            Log.d(TAG, "messageArrived: msg:" + message.toString());
            new Thread(() -> {
                Message msg = MessageProcessor.decryptMessage(message.toString());
                if (msg != null) {
                    processMessage(msg);
                    Log.d(TAG, "run: 信息:" + msg.getMsg());
                    Log.d(TAG, "run: 发送时间:" + new Date(msg.getSendTime()));
                } else {
                    Log.e(TAG, "run: 解密失败");
                }
            }).start();
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d(TAG, "deliveryComplete: 消息已发送");
        }
    }
}
