package com.chzu.ice.schat.activities.auth.register;

import android.util.Log;

import com.chzu.ice.schat.data.RemoteDatabase;
import com.chzu.ice.schat.pojos.gson.resp.BaseResponse;

public class ImplRegisterPresenter implements RegisterContract.Presenter {
    private static final String TAG = ImplRegisterPresenter.class.getSimpleName();

    private RegisterContract.View view;

    ImplRegisterPresenter(RegisterContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void register(String username, String password) {
        view.afterRegister();
        new Thread(() -> {
            BaseResponse respJ = RemoteDatabase.remoteRegister(username, password);
            if (respJ != null) {
                view.endRegister();
                switch (respJ.code) {
                    case "10101":
                        view.showRegisterSucceed();
                        break;
                    case "10102":
                        view.showRegisterFailedForUsernameExist();
                        break;
                    case "10103":
                        break;
                }
            } else {
                Log.e(TAG, "remoteRegister: response无法被解析");
            }
        }).start();
    }
}
