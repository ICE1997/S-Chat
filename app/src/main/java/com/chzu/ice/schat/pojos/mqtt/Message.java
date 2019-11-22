package com.chzu.ice.schat.pojos.mqtt;

public class Message {
    private String sender;
    private String receiver;
    private String msg;
    private long sendTime;

    public Message() {
    }

    public Message(String msg, long sendTime) {
        this.msg = msg;
        this.sendTime = sendTime;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
