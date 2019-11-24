package com.chzu.ice.schat.data;

import android.content.Context;
import android.util.Log;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.pojos.database.AccountE;
import com.chzu.ice.schat.utils.SPHelper;

public class SPDao {
    public static final String SP_CONFIG_KEY_IS_FIRST_OPEN = "is_first_open";
    private static final String SP_CONFIG_ADDRESS_LOGIN_INFO = "login_info";
    private static final String SP_CONFIG_KEY_SIGNED_IN_USERNAME = "signed_in_username";
    private static final String SP_CONFIG_KEY_SIGNED_IN_USER_TOPIC = "signed_in_user_topic";
    private static final String SP_CONFIG_KEY_SIGNED_IN_USER_PUBLIC_KEY = "signed_in_user_private_key";
    private static final String SP_CONFIG_KEY_SIGNED_IN_USER_PRIVATE_KEY = "signed_in_user_public_key";
    private static final String SP_CONFIG_KEY_SIGNED_IN_USER_ACCESS_TOKEN = "signed_in_user_access_token";
    private static final String SP_CONFIG_KEY_SIGNED_IN_USER_REFRESH_TOKEN = "signed_in_user_refresh_token";
    private static final String SP_CONFIG_KEY_HAS_SIGNED_IN = "has_signed_in";
    private static final String TAG = SPDao.class.getSimpleName();

    public static boolean getHasSignedIn() {
        Context context = App.getContext();
        SPHelper spHelper = new SPHelper(context, SP_CONFIG_ADDRESS_LOGIN_INFO);
        return spHelper.getBoolean(SP_CONFIG_KEY_HAS_SIGNED_IN, false);
    }

    public static void setSignedIn(boolean isSignedIn) {
        Context context = App.getContext();
        SPHelper spHelper = new SPHelper(context, SP_CONFIG_ADDRESS_LOGIN_INFO);
        spHelper.putBoolean(SP_CONFIG_KEY_HAS_SIGNED_IN, isSignedIn);
        spHelper.apply();
    }

    public static void setSignedInUser(AccountE account) {
        Context context = App.getContext();
        if (context != null) {
            SPHelper spHelper = new SPHelper(context, SP_CONFIG_ADDRESS_LOGIN_INFO);
            spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USERNAME, account.getUsername());
            spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_TOPIC, account.getTopic());
            spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_PUBLIC_KEY, account.getPublicKey());
            spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_PRIVATE_KEY, account.getPrivateKey());
            spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_ACCESS_TOKEN, account.getAccessToken());
            spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_REFRESH_TOKEN, account.getRefreshToken());
            spHelper.apply();
        } else {
            Log.e(TAG, "setSignedInUser: 获取Context失败");
        }
    }

    public static void setSignedInUsername(String signedInUsername) {
        SPHelper spHelper = new SPHelper(App.getContext(), SP_CONFIG_ADDRESS_LOGIN_INFO);
        spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USERNAME, signedInUsername);
        spHelper.apply();
    }

    public static void setSignedInUserTopic(String signedInUserTopic) {
        SPHelper spHelper = new SPHelper(App.getContext(), SP_CONFIG_ADDRESS_LOGIN_INFO);
        spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_TOPIC, signedInUserTopic);

        spHelper.apply();
    }

    public static void setSignedInUserPublicKey(String signedInUserPublicKey) {
        SPHelper spHelper = new SPHelper(App.getContext(), SP_CONFIG_ADDRESS_LOGIN_INFO);
        spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_PUBLIC_KEY, signedInUserPublicKey);

        spHelper.apply();
    }

    public static void setSignedInUserPrivateKey(String signedInUserPrivateKey) {
        SPHelper spHelper = new SPHelper(App.getContext(), SP_CONFIG_ADDRESS_LOGIN_INFO);
        spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_PRIVATE_KEY, signedInUserPrivateKey);

        spHelper.apply();
    }

    public static void setSignedInUserAccessToken(String signedInUserAccessToken) {
        SPHelper spHelper = new SPHelper(App.getContext(), SP_CONFIG_ADDRESS_LOGIN_INFO);
        spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_ACCESS_TOKEN, signedInUserAccessToken);

        spHelper.apply();
    }

    public static void setSignedInUserRefreshsToken(String signedInUserRefeshToken) {
        SPHelper spHelper = new SPHelper(App.getContext(), SP_CONFIG_ADDRESS_LOGIN_INFO);
        spHelper.putString(SP_CONFIG_KEY_SIGNED_IN_USER_REFRESH_TOKEN, signedInUserRefeshToken);

        spHelper.apply();
    }

    public static String getSignedInUsername(Context context) {
        SPHelper spHelper = new SPHelper(context, SP_CONFIG_ADDRESS_LOGIN_INFO);
        return spHelper.getString(SP_CONFIG_KEY_SIGNED_IN_USERNAME, null);
    }

    public static String getSignedInUserTopic(Context context) {
        SPHelper spHelper = new SPHelper(context, SP_CONFIG_ADDRESS_LOGIN_INFO);
        return spHelper.getString(SP_CONFIG_KEY_SIGNED_IN_USER_TOPIC, null);
    }

    public static String getSignedInUserPublicKey(Context context) {
        SPHelper spHelper = new SPHelper(context, SP_CONFIG_ADDRESS_LOGIN_INFO);
        return spHelper.getString(SP_CONFIG_KEY_SIGNED_IN_USER_PUBLIC_KEY, null);
    }

    public static String getSignedInUserPrivateKey(Context context) {
        SPHelper spHelper = new SPHelper(context, SP_CONFIG_ADDRESS_LOGIN_INFO);
        return spHelper.getString(SP_CONFIG_KEY_SIGNED_IN_USER_PRIVATE_KEY, null);
    }

    public static String getSignedInUserAccessToken(Context context) {
        SPHelper spHelper = new SPHelper(context, SP_CONFIG_ADDRESS_LOGIN_INFO);
        return spHelper.getString(SP_CONFIG_KEY_SIGNED_IN_USER_ACCESS_TOKEN, null);
    }

    public static String getSignedInUserRefreshToken(Context context) {
        SPHelper spHelper = new SPHelper(context, SP_CONFIG_ADDRESS_LOGIN_INFO);
        return spHelper.getString(SP_CONFIG_KEY_SIGNED_IN_USER_REFRESH_TOKEN, null);
    }
}
