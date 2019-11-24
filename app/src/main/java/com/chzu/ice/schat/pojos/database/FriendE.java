package com.chzu.ice.schat.pojos.database;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class FriendE {
    @Id
    public long id;
    private String username;
    private String friendName;
    private String friendTopic;
    private String friendAvatarSrc;
    private String publicKey;


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

    public String getFriendTopic() {
        return friendTopic;
    }

    public void setFriendTopic(String friendTopic) {
        this.friendTopic = friendTopic;
    }

    public String getFriendAvatarSrc() {
        return friendAvatarSrc;
    }

    public void setFriendAvatarSrc(String friendAvatarSrc) {
        this.friendAvatarSrc = friendAvatarSrc;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
