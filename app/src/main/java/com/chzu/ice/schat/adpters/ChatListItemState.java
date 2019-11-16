package com.chzu.ice.schat.adpters;

class ChatListItemState {
    private boolean isSelected;
    private boolean isLeftOpened;
    private boolean isRead;
    private int newMessageNumber;
    private String messageSender;
    private String latestMessage;
    private String receivedTime;

    public ChatListItemState(boolean isSelected, boolean isLeftOpened, boolean isRead, int newMessageNumber, String messageSender, String latestMessage, String receivedTime) {
        this.isSelected = isSelected;
        this.isLeftOpened = isLeftOpened;
        this.isRead = isRead;
        this.newMessageNumber = newMessageNumber;
        this.messageSender = messageSender;
        this.latestMessage = latestMessage;
        this.receivedTime = receivedTime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
