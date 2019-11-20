package com.chzu.ice.schat.adpters;

public class ChatItem {
    private String content;
    private String time;
    private boolean isSender;


    public ChatItem(String content, String time, boolean isSender) {
        this.content = content;
        this.time = time;
        this.isSender = isSender;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
