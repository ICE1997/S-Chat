package com.chzu.ice.schat;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.chzu.ice.schat.data.LocalRepository;
import com.chzu.ice.schat.data.SPDao;
import com.chzu.ice.schat.pojos.database.AccountE;
import com.chzu.ice.schat.services.MQTTService;
import com.chzu.ice.schat.utils.ObjectBoxHelper;

import java.util.List;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static Context mContext;
    private static String signedInUsername;
    private static String signedInUserTopic;
    private static String signedInUserPrivateKey;
    private static String signedInUserPublicKey;
    private static String signedInUserAccessToken;
    private static String signedInUserRefreshToken;

    public static Context getContext() {
        return mContext;
    }

    public static boolean getHasSignedIn() {
        return SPDao.getHasSignedIn();
    }

    public static void setSignedIn(boolean isSignedIn) {
        SPDao.setSignedIn(isSignedIn);
    }

    public static void logOut() {
        signedInUsername = null;
        signedInUserTopic = null;
        signedInUserPrivateKey = null;
        signedInUserPublicKey = null;
        signedInUserAccessToken = null;
        signedInUserRefreshToken = null;

        SPDao.setSignedInUsername(null);
        SPDao.setSignedInUserTopic(null);
        SPDao.setSignedInUserAccessToken(null);
        SPDao.setSignedInUserRefreshsToken(null);
        SPDao.setSignedInUserPublicKey(null);
        SPDao.setSignedInUserPrivateKey(null);

        setSignedIn(false);
        SPDao.setSignedInUser(new AccountE());
    }

    public static void addAccount(AccountE account) {
        signedInUsername = account.getUsername();
        signedInUserTopic = account.getTopic();
        signedInUserPrivateKey = account.getPrivateKey();
        signedInUserPublicKey = account.getPublicKey();
        signedInUserAccessToken = account.getAccessToken();
        signedInUserRefreshToken = account.getRefreshToken();
        SPDao.setSignedInUser(account);
        LocalRepository.localAddAccount(account);
    }

    public static String getSignedInUsername() {
        if (signedInUsername == null) {
            signedInUsername = SPDao.getSignedInUsername(mContext);
        }
        return signedInUsername;
    }

    public static String getSignedInUserTopic() {
        if (signedInUserTopic == null) {
            signedInUserTopic = SPDao.getSignedInUserTopic(mContext);
            if (signedInUserTopic == null) {
                AccountE account = LocalRepository.localGetAccountByUsername(getSignedInUsername());
                signedInUserTopic = account.getTopic();
            }
        }
        return signedInUserTopic;
    }

    public static String getSignedInUserPrivateKey() {
        if (signedInUserPrivateKey == null) {
            signedInUserPrivateKey = SPDao.getSignedInUserPrivateKey(mContext);
            if (signedInUserPrivateKey == null) {
                if (getSignedInUsername() != null) {
                    AccountE account = LocalRepository.localGetAccountByUsername(getSignedInUsername());
                    signedInUserPrivateKey = account.getPrivateKey();
                }
            }
        }
        return signedInUserPrivateKey;
    }

    public static String getSignedInUserPublicKey() {
        if (signedInUserPublicKey == null) {
            signedInUserPublicKey = SPDao.getSignedInUserPublicKey(mContext);
            if (signedInUserPublicKey == null) {
                if (getSignedInUsername() != null) {
                    AccountE account = LocalRepository.localGetAccountByUsername(getSignedInUsername());
                    signedInUserPublicKey = account.getPublicKey();
                }
            }
        }
        return signedInUserPublicKey;
    }

    public static String getSignedInUserAccessToken() {
        if (signedInUserAccessToken == null) {
            signedInUserAccessToken = SPDao.getSignedInUserAccessToken(mContext);
            if (signedInUserAccessToken == null) {
                if (getSignedInUsername() != null) {
                    AccountE account = LocalRepository.localGetAccountByUsername(getSignedInUsername());
                    signedInUserAccessToken = account.getAccessToken();
                }
            }
        }
        return signedInUserAccessToken;
    }

    public static String getSignedInUserRefreshToken() {
        if (signedInUserRefreshToken == null) {
            signedInUserRefreshToken = SPDao.getSignedInUserRefreshToken(mContext);
            if (signedInUserRefreshToken == null) {
                if (getSignedInUsername() != null) {
                    AccountE account = LocalRepository.localGetAccountByUsername(getSignedInUsername());
                    signedInUserRefreshToken = account.getRefreshToken();
                }
            }
        }
        return signedInUserRefreshToken;
    }

    public static boolean isMQTTServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningServiceInfo> runningServiceInfoList = activityManager.getRunningServices(100);
            for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfoList) {
                String classname = serviceInfo.service.getClassName();
                Log.d(TAG, "isMQTTServiceRunning: " + classname);
                if ("MQTTService".equals(classname)) {
                    return true;
                }
            }
        } else {
            Log.d(TAG, "isMQTTServiceRunning: 无法获取activityManager");
        }
        return false;
    }

    public void startMQTTService() {
        if (!isMQTTServiceRunning()) {
            Intent intent = new Intent(this, MQTTService.class);
            startService(intent);
        } else {
            Log.d(TAG, "startMQTTService: 服务已启动");
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "消息通知";
            String description = "用于消息通知，请开启。";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("SChat Notification", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        createNotificationChannel();
        ObjectBoxHelper.init(this);
        ObjectBoxHelper.startDebug(true, this);
        startMQTTService();
    }
}
