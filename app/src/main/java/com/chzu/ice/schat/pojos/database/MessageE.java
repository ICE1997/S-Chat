package com.chzu.ice.schat.pojos.database;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class MessageE {
    @Id
    public long id;
    private boolean isUserSend;
    private String sender;
    private String receiver;
    private String content;
    private long timestamp;



    public boolean isUserSend() {
        return isUserSend;
    }

    public void setUserSend(boolean userSend) {
        isUserSend = userSend;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
