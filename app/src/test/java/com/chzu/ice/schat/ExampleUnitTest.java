package com.chzu.ice.schat;

import com.chzu.ice.schat.pojos.mqtt.Message;
import com.chzu.ice.schat.pojos.mqtt.MessageWrapper;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        Gson gson = new Gson();
        MessageWrapper messageWrapper = new MessageWrapper("1", "2");
        messageWrapper.setMessage(new Message("1fa", 1313L));
        String s = gson.toJson(messageWrapper);
        MessageWrapper messageWrapper1 = gson.fromJson(s, MessageWrapper.class);
        Assert.assertEquals(messageWrapper.getMessage().getMsg(), messageWrapper1.getMessage().getMsg());
    }

}