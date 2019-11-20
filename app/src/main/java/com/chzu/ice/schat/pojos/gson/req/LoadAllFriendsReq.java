package com.chzu.ice.schat.pojos.gson.req;

public class LoadAllFriendsReq {
    private String username;

    public LoadAllFriendsReq(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
