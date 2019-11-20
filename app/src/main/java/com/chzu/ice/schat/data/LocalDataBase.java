package com.chzu.ice.schat.data;

import android.util.Log;

import com.chzu.ice.schat.pojos.database.AccountE;
import com.chzu.ice.schat.pojos.database.AccountE_;
import com.chzu.ice.schat.pojos.database.FriendE;
import com.chzu.ice.schat.pojos.database.FriendE_;
import com.chzu.ice.schat.utils.ObjectBoxHelper;

import java.util.List;

import io.objectbox.Box;

public class LocalDataBase {
    private static final String TAG = LocalDataBase.class.getSimpleName();

    private LocalDataBase() {
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

    /**
     * 根据用户名来获取此用户的所有朋友信息
     */
    public static List<FriendE> localGetAllFriendsByUsername(String username) {
        Box<FriendE> friendEBox = ObjectBoxHelper.get().boxFor(FriendE.class);
        return friendEBox.query().equal(FriendE_.username, username).build().find();
    }

}
