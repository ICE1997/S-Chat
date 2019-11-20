package com.chzu.ice.schat.adpters;

class ChatListItem {

    private boolean isLeftOpened;
    private boolean isRead;
    private int position;
    private int unReadMessageNumber;
    private String messageSender;
    private String latestMessage;
    private String receivedTime;

    public ChatListItem(int position, boolean isLeftOpened, boolean isRead, int unReadMessageNumber, String messageSender, String latestMessage, String receivedTime) {
        this.position = position;
        this.isLeftOpened = isLeftOpened;
        this.isRead = isRead;
        this.unReadMessageNumber = unReadMessageNumber;
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
