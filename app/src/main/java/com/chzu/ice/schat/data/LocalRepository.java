package com.chzu.ice.schat.data;

import android.util.Log;

import com.chzu.ice.schat.pojos.database.AccountE;
import com.chzu.ice.schat.pojos.database.AccountE_;
import com.chzu.ice.schat.pojos.database.ChatListE;
import com.chzu.ice.schat.pojos.database.ChatListE_;
import com.chzu.ice.schat.pojos.database.FriendE;
import com.chzu.ice.schat.pojos.database.FriendE_;
import com.chzu.ice.schat.pojos.database.MessageE;
import com.chzu.ice.schat.pojos.database.MessageE_;
import com.chzu.ice.schat.utils.ObjectBoxHelper;

import java.util.List;

import io.objectbox.Box;

public class LocalRepository {
    private static final String TAG = LocalRepository.class.getSimpleName();

    private LocalRepository() {
    }

    /**
     * 本地数据库添加账户
     */
    public static void localAddAccount(AccountE account) {
        Box<AccountE> accountEBox = ObjectBoxHelper.get().boxFor(AccountE.class);
        AccountE temp = accountEBox.query().equal(AccountE_.username, account.getUsername()).build().findFirst();
        if (temp == null) {
            accountEBox.put(account);
        } else {
            Log.e(TAG, "localAddAccount: 账户已存在！调用localUpdate()方法");
            localUpdateAccount(account);
        }
    }


    /**
     * 本地数据库删除账户
     */
    public static void localRemoveAccount(AccountE account) {
        Box<AccountE> accountEBox = ObjectBoxHelper.get().boxFor(AccountE.class);
        AccountE temp = accountEBox.query().equal(AccountE_.username, account.getUsername()).build().findFirst();
        if (temp == null) {
            Log.e(TAG, "updateAccountLocally: 此账户不存在！");
        } else {
            accountEBox.remove(account);
        }
    }

    /**
     * 本地数据库更新账户
     */
    public static void localUpdateAccount(AccountE accountNew) {
        Box<AccountE> accountEBox = ObjectBoxHelper.get().boxFor(AccountE.class);
        AccountE temp = accountEBox.query().equal(AccountE_.username, accountNew.getUsername()).build().findFirst();
        if (temp == null) {
            Log.e(TAG, "updateAccountLocally: 此账户不存在！");
        } else {
            temp.setAccessToken(accountNew.getAccessToken());
            temp.setPrivateKey(accountNew.getPrivateKey());
            temp.setPublicKey(accountNew.getPublicKey());
            temp.setRefreshToken(accountNew.getRefreshToken());
            temp.setTopic(accountNew.getTopic());
            temp.setUsername(accountNew.getUsername());
            accountEBox.put(temp);
        }
    }

    /**
     * 通过用户名来获取账户的所有相关信息
     */
    public static AccountE localGetAccountByUsername(String username) {
        Box<AccountE> accountEBox = ObjectBoxHelper.get().boxFor(AccountE.class);
        return accountEBox.query().equal(AccountE_.username, username).build().findFirst();
    }


    /**
     * 增加一个朋友
     */
    public static void localAddFriend(FriendE friend) {
        Box<FriendE> friendEBox = ObjectBoxHelper.get().boxFor(FriendE.class);
        FriendE temp = friendEBox.query().equal(FriendE_.username, friend.getUsername()).equal(FriendE_.friendName, friend.getFriendName()).build().findFirst();
        if (temp == null) {
            friendEBox.put(friend);
        } else {
            Log.e(TAG, "localAddAccount: 好友已存在！call localUpdateFriend()");
            localUpdateFriend(friend);
        }
    }

    /**
     * 增加所有朋友
     * TODO:此接口用于前期使用，后期将采用缓存机制。
     */
    public static void localAddAllFriends(List<FriendE> friends) {
        Box<FriendE> friendEBox = ObjectBoxHelper.get().boxFor(FriendE.class);
        friendEBox.put(friends);
    }

    /**
     * 更新朋友信息
     */
    public static void localUpdateFriend(FriendE friendNew) {
        Box<FriendE> friendEBox = ObjectBoxHelper.get().boxFor(FriendE.class);
        FriendE temp = friendEBox.query().equal(FriendE_.username, friendNew.getUsername()).equal(FriendE_.friendName, friendNew.getFriendName()).build().findFirst();
        if (temp == null) {
            Log.e(TAG, "updateAccountLocally: 此好友不存在！");
        } else {
            temp.setUsername(friendNew.getUsername());
            temp.setFriendAvatarSrc(friendNew.getFriendAvatarSrc());
            temp.setFriendName(friendNew.getFriendName());
            temp.setFriendTopic(friendNew.getFriendTopic());
            temp.setPublicKey(friendNew.getPublicKey());
            friendEBox.put(temp);
        }
    }

    /**
     * 根据用户名和朋友的用户名来获取这个朋友关系
     */
    public static FriendE localGetFriendByUsernameAndFriendName(String username, String friendName) {
        Box<FriendE> friendEBox = ObjectBoxHelper.get().boxFor(FriendE.class);
        return friendEBox.query().equal(FriendE_.username, username).equal(FriendE_.friendName, friendName).build().findFirst();
    }

    public static FriendE localGetFriendByUsernameAndFriendTopic(String username, String topic) {
        Box<FriendE> friendEBox = ObjectBoxHelper.get().boxFor(FriendE.class);
        return friendEBox.query().equal(FriendE_.username, username).equal(FriendE_.friendTopic, topic).build().findFirst();
    }

    /**
     * 根据用户名来获取此用户的所有朋友信息
     */
    public static List<FriendE> localGetAllFriendsByUsername(String username) {
        Box<FriendE> friendEBox = ObjectBoxHelper.get().boxFor(FriendE.class);
        return friendEBox.query().equal(FriendE_.username, username).build().find();
    }

    public static void localAddMessage(MessageE messageE) {
        Box<MessageE> messageEBox = ObjectBoxHelper.get().boxFor(MessageE.class);
        messageEBox.put(messageE);
    }

    public static List<MessageE> localGetAllMessagesByUsernameAndFriendName(String username, String friendName) {
        Box<MessageE> messageEBox = ObjectBoxHelper.get().boxFor(MessageE.class);
        return messageEBox.query().equal(MessageE_.sender, username).or().equal(MessageE_.receiver, username).and().equal(MessageE_.sender, friendName).or().equal(MessageE_.receiver, friendName).build().find();
    }

    public static void localAddOrUpdateChatList(ChatListE chatListE) {
        Box<ChatListE> chatListEBox = ObjectBoxHelper.get().boxFor(ChatListE.class);
        ChatListE temp = chatListEBox.query().equal(ChatListE_.username, chatListE.getUsername()).and().equal(ChatListE_.friendName, chatListE.getFriendName()).build().findFirst();
        if (temp == null) {
            Log.d(TAG, "localAddOrUpdateChatList: 无此对话，新建对话");
            chatListEBox.put(chatListE);
        } else {
            temp.setFriendName(chatListE.getFriendName());
            temp.setLatestChatTime(chatListE.getLatestChatTime());
            temp.setLatestMsg(chatListE.getLatestMsg());
            temp.setUnReadMessageNum(chatListE.getUnReadMessageNum());
            temp.setUsername(chatListE.getUsername());
            temp.setAvatarSrc(chatListE.getAvatarSrc());
            chatListEBox.put(temp);
        }
    }

    public static void deleteChatlistByUsernameAndFriendName(String username, String friendName) {
        Box<ChatListE> chatListEBox = ObjectBoxHelper.get().boxFor(ChatListE.class);
        ChatListE chatListE = chatListEBox.query().equal(ChatListE_.username, username).and().equal(ChatListE_.friendName, friendName).build().findFirst();
        if (chatListE != null) {
            chatListEBox.remove(chatListE);
        } else {
            Log.d(TAG, "deleteChatlistByUsernameAndFriendName: 删除失败，并无此对话");
        }
    }

    public static ChatListE localGetChatListByUsernameAndFriendName(String username, String friendName) {
        Box<ChatListE> chatListEBox = ObjectBoxHelper.get().boxFor(ChatListE.class);
        return chatListEBox.query().equal(ChatListE_.username, username).and().equal(ChatListE_.friendName, friendName).build().findFirst();
    }

    public static List<ChatListE> localGetAllChatListByUsername(String username) {
        Box<ChatListE> chatListEBox = ObjectBoxHelper.get().boxFor(ChatListE.class);
        return chatListEBox.query().equal(ChatListE_.username, username).build().find();
    }

    public static void localSetChatReadByUsernameAndFriendName(String username, String friendName) {
        Box<ChatListE> chatListEBox = ObjectBoxHelper.get().boxFor(ChatListE.class);
        ChatListE chatListE = chatListEBox.query().equal(ChatListE_.username, username).and().equal(ChatListE_.friendName, friendName).build().findFirst();
        if (chatListE != null) {
            chatListE.setUnReadMessageNum(0);
            localAddOrUpdateChatList(chatListE);
        } else {
            Log.d(TAG, "localSetChatReadByUsernameAndFriendName: 无此对话");
        }
    }
}
