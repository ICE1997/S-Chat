package com.chzu.ice.schat.adpters;

public class ChatListItem {
    private int unReadMessageNumber;
    private String messageSender;
    private String latestMessage;
    private String receivedTime;

    public ChatListItem(int unReadMessageNumber, String messageSender, String latestMessage, String receivedTime) {
        this.unReadMessageNumber = unReadMessageNumber;
        this.messageSender = messageSender;
        this.latestMessage = latestMessage;
        this.receivedTime = receivedTime;
    }


    public boolean isRead() {
        return !(unReadMessageNumber > 0);
    }


    public int getUnReadMessageNumber() {
        return unReadMessageNumber;
    }

    public void setUnReadMessageNumber(int unReadMessageNumber) {
        this.unReadMessageNumber = unReadMessageNumber;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }
}
