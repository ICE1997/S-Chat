package com.chzu.ice.schat.activities.auth.login;

import android.util.Log;

import com.chzu.ice.schat.App;
import com.chzu.ice.schat.data.LocalRepository;
import com.chzu.ice.schat.data.RemoteRepository;
import com.chzu.ice.schat.data.SPDao;
import com.chzu.ice.schat.pojos.database.AccountE;
import com.chzu.ice.schat.pojos.gson.resp.BaseResponse;
import com.chzu.ice.schat.pojos.gson.resp.data.LoginData;
import com.chzu.ice.schat.utils.RSAUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ImplLoginPresenter implements LoginContract.Presenter {
    private static final String TAG = ImplLoginPresenter.class.getSimpleName();
    public LoginContract.View view;

    ImplLoginPresenter(LoginContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void login(final String username, final String password) {
        view.afterLogin();
        new Thread(() -> {
            KeyPair keyPair = RSAUtil.generateKeyPair(RSAUtil.KEYSIZE);
            PublicKey publicKey;
            PrivateKey privateKey;
            if (keyPair != null) {
                publicKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();
                BaseResponse<LoginData> respJ = RemoteRepository.remoteLogin(username, password, RSAUtil.byte2Base64(publicKey.getEncoded()));
                if (respJ != null) {
                    view.endLogin();
                    switch (respJ.code) {
                        case "10201":
                            view.showLoginSucceed();
                            App.setSignedIn(true);
                            LoginData data = respJ.data;
                            AccountE accountE = new AccountE();
                            accountE.setUsername(username);
                            accountE.setRefreshToken(data.refreshToken);
                            accountE.setAccessToken(data.accessToken);
                            accountE.setTopic(data.topic);
                            accountE.setPublicKey(RSAUtil.byte2Base64(publicKey.getEncoded()));
                            accountE.setPrivateKey(RSAUtil.byte2Base64(privateKey.getEncoded()));
                            LocalRepository.localAddAccount(accountE);
                            SPDao.setSignedInUser(accountE);
                            break;
                        case "10202":
                            Log.i(TAG, "run: 用户名不存在！");
                            view.showLoginFailedForWrongUsernameOrPassword();
                            break;
                        case "10203":
                            view.showLoginFailedForWrongUsernameOrPassword();
                            break;
                        case "401": {
                            view.showLoginFailedForWrongUsernameOrPassword();
                        }
                    }
                } else {
                    Log.e(TAG, "remoteLogin: 返回结果无法被解析");
                }
            } else {
                Log.e(TAG, "remoteLogin: 密钥对生成失败！");
            }
        }).start();
    }
}
