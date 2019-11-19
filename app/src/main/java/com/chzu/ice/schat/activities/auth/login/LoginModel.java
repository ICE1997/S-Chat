package com.chzu.ice.schat.activities.auth.login;

import android.util.Log;

import com.chzu.ice.schat.configs.AppConfig;
import com.chzu.ice.schat.pojos.gson.req.LoginReq;
import com.chzu.ice.schat.pojos.gson.resp.BaseResponse;
import com.chzu.ice.schat.pojos.gson.resp.data.LoginData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class LoginModel {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String TAG = LoginModel.class.getSimpleName();

    private LoginModel() {
    }

    static void login(final String username, final String password, final String publicKey, final LoginCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();

        new Thread(() -> {
            Gson gson = new Gson();
            String loginReqJson = gson.toJson(new LoginReq(username, password, publicKey));
            RequestBody requestBody = RequestBody.create(loginReqJson, JSON);
            Request request = new Request.Builder().url(AppConfig.LOGIN_API).post(requestBody).build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String respS = Objects.requireNonNull(response.body()).string();
                BaseResponse<LoginData> respJ = gson.fromJson(respS, new TypeToken<BaseResponse<LoginData>>() {
                }.getType());
                Log.i(TAG, "login: "+respS);
                switch (respJ.code) {
                    case "10201":
                        callback.loginSucceed(respJ.data.accessToken, respJ.data.refreshToken, respJ.data.topic);
                        break;
                    case "10202":
                        callback.noSuchUser();
                        break;
                    case "10203":
                        callback.wrongPassword();
                        break;
                    case "401":{
                        callback.wrongPassword();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    interface LoginCallback {
        void noSuchUser();

        void wrongPassword();

        void loginSucceed(String accessToken, String refreshToken, String topic);
    }
}
