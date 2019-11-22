package com.chzu.ice.schat.pojos.database;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class ChatListE {
    @Id
    public long id;
    private String username;
    private String friendName;
    private int unReadMessageNum;
    private String latestMsg;
    private long latestChatTime;
    private String avatarSrc;

    public String getAvatarSrc() {
        return avatarSrc;
    }

    public void setAvatarSrc(String avatarSrc) {
        this.avatarSrc = avatarSrc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public int getUnReadMessageNum() {
        return unReadMessageNum;
    }

    public void setUnReadMessageNum(int unReadMessageNum) {
        this.unReadMessageNum = unReadMessageNum;
    }

    public String getLatestMsg() {
        return latestMsg;
    }

    public void setLatestMsg(String latestMsg) {
        this.latestMsg = latestMsg;
    }

    public long getLatestChatTime() {
        return latestChatTime;
    }

    public void setLatestChatTime(long latestChatTime) {
        this.latestChatTime = latestChatTime;
    }
}
