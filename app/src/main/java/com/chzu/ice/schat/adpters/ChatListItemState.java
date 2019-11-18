package com.chzu.ice.schat.adpters;

class ChatListItemState {

    private boolean isLeftOpened;
    private boolean isRead;
    private int position;
    private int newMessageNumber;
    private String messageSender;
    private String latestMessage;
    private String receivedTime;

    public ChatListItemState(int position, boolean isLeftOpened, boolean isRead, int newMessageNumber, String messageSender, String latestMessage, String receivedTime) {
        this.position = position;
        this.isLeftOpened = isLeftOpened;
        this.isRead = isRead;
        this.newMessageNumber = newMessageNumber;
        this.messageSender = messageSender;
        this.latestMessage = latestMessage;
        this.receivedTime = receivedTime;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isLeftOpened() {
        return isLeftOpened;
    }

    public void setLeftOpened(boolean leftOpened) {
        isLeftOpened = leftOpened;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean hasNewMessage) {
        this.isRead = hasNewMessage;
    }

    public int getNewMessageNumber() {
        return newMessageNumber;
    }

    public void setNewMessageNumber(int newMessageNumber) {
        this.newMessageNumber = newMessageNumber;
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
