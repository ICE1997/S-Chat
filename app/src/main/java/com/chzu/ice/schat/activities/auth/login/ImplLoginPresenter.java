package com.chzu.ice.schat.activities.auth.login;

import android.util.Log;

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
    public void login(String username, String password) {
        KeyPair keyPair = RSAUtil.generateRSAKeyPair(4096);
        PublicKey publicKey;
        PrivateKey privateKey = null;
        if (keyPair != null) {
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            LoginModel.login(username, password, new String(publicKey.getEncoded()), new LoginModel.LoginCallback() {
                @Override
                public void noSuchUser() {
                    Log.i(TAG, "noSuchUser: ");
                    view.showLoginFailedForWrongUsernameOrPassword();
                }

                @Override
                public void wrongPassword() {
                    Log.i(TAG, "wrongPassword: ");
                    view.showLoginFailedForWrongUsernameOrPassword();
                }

                @Override
                public void loginSucceed(String accessToken, String refreshToken, String topic) {
                    view.showLoginSucceed();
                }
            });
        } else {
            Log.e(TAG, "login: 密钥对生成失败！");
        }
    }
}
