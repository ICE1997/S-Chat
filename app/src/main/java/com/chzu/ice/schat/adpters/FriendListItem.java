package com.chzu.ice.schat.adpters;

public class FriendListItem {
    private String avatarAddr;
    private String username;


    public FriendListItem(String avatarAddr, String username) {
        this.avatarAddr = avatarAddr;
        this.username = username;
    }

    public String getAvatarAddr() {
        return avatarAddr;
    }

    public void setAvatarAddr(String avatarAddr) {
        this.avatarAddr = avatarAddr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
