package com.chzu.ice.schat.utils;

import android.util.Log;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.data.LocalRepository;
import com.chzu.ice.schat.pojos.database.FriendE;
import com.chzu.ice.schat.pojos.mqtt.Message;
import com.chzu.ice.schat.pojos.mqtt.MessageWrapper;
import com.google.gson.Gson;

public class MessageProcessor {
    private static final String TAG = MessageProcessor.class.getSimpleName();

    public static Message decryptMessage(String message) {
        byte[] decodeWithBase64 = RSAUtil.base642Byte(message);
        try {
            Gson gson = new Gson();
            byte[] decryptedByPrivateKey = RSAUtil.decryptWithPrivateKeyBlock(decodeWithBase64, RSAUtil.base642Byte(App.getSignedInUserPrivateKey()));

            MessageWrapper msg = gson.fromJson(new String(decryptedByPrivateKey), MessageWrapper.class);
            Log.d(TAG, "messageArrived 发送者: " + msg.getSender());

            String content = msg.getContent();
            Log.d(TAG, "messageArrived: 加密内容:" + content);

            String friendPublicKey = LocalRepository.localGetFriendByUsernameAndFriendName(App.getSignedInUsername(), msg.getSender()).getPublicKey();
            Log.d(TAG, "messageArrived: friendpublickey\n" + friendPublicKey);

            byte[] decryptedByPublicKey = RSAUtil.decryptWithPublicKeyBlock(RSAUtil.base642Byte(content), RSAUtil.base642Byte(friendPublicKey));

            return gson.fromJson(new String(decryptedByPublicKey), Message.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptMessage(Message message, FriendE toFriend) throws Exception {
        Gson gson = new Gson();

        String msgContent = gson.toJson(message);

        byte[] encryptedByPrivateKey = RSAUtil.encryptWithPrivateKeyBlock(msgContent.getBytes(), RSAUtil.base642Byte(App.getSignedInUserPrivateKey()));

        MessageWrapper messageWrapper = new MessageWrapper(App.getSignedInUsername(), RSAUtil.byte2Base64(encryptedByPrivateKey));
        String msg = gson.toJson(messageWrapper);

        byte[] encryptedByPublicKey = RSAUtil.encryptWithPublicKeyBlock(msg.getBytes(), RSAUtil.base642Byte(toFriend.getPublicKey()));

        return RSAUtil.byte2Base64(encryptedByPublicKey);
    }
}
