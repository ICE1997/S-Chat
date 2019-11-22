package com.chzu.ice.schat.data;

import android.util.Log;

import com.chzu.ice.schat.pojos.mqtt.Message;
import com.chzu.ice.schat.pojos.mqtt.MessageWrapper;
import com.google.gson.Gson;

import org.junit.Test;

public class RestApiTest {
    private static final String TAG = RestApiTest.class.getSimpleName();

    @Test
    public void login() {
        Gson gson = new Gson();
        MessageWrapper messageWrapper = new MessageWrapper("1", "2");
        messageWrapper.setMessage(new Message("1fa", 1313L));

        String s = gson.toJson(messageWrapper);

        MessageWrapper messageWrapper1 = gson.fromJson(s, MessageWrapper.class);

        Log.d(TAG, "login: " + messageWrapper1.getMessage().getMsg());


        assert messageWrapper.getMessage().getMsg().equals(messageWrapper1.getMessage().getMsg());


    }
}