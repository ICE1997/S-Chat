package com.chzu.ice.schat.data;

import android.util.Log;

import com.chzu.ice.schat.configs.AppConfig;
import com.chzu.ice.schat.pojos.gson.req.LoginReq;
import com.chzu.ice.schat.pojos.gson.req.RegisterReq;
import com.chzu.ice.schat.pojos.gson.resp.BaseResponse;
import com.chzu.ice.schat.pojos.gson.resp.data.LoadAllFriendRelationsData;
import com.chzu.ice.schat.pojos.gson.resp.data.LoginData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class RemoteRepository {
    private static final String TAG = RemoteRepository.class.getSimpleName();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static BaseResponse<LoginData> remoteLogin(final String username, final String password, final String publicKey) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        LoginReq loginReq = new LoginReq(username, password, publicKey);
        String loginReqJson = gson.toJson(loginReq);
        Log.i(TAG, "remoteLogin: " + loginReqJson);
        RequestBody requestBody = RequestBody.create(loginReqJson, JSON);
        Request request = new Request.Builder().url(AppConfig.LOGIN_API).post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String respS = Objects.requireNonNull(response.body()).string();
            Log.i(TAG, "remoteLogin: " + respS);
            BaseResponse<LoginData> respJ = gson.fromJson(respS, new TypeToken<BaseResponse<LoginData>>() {
            }.getType());
            return respJ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BaseResponse remoteRegister(final String usr, final String psw) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        RegisterReq re = new RegisterReq(usr, psw);
        String act = gson.toJson(re);
        Log.i(TAG, "remoteRegister: " + act);
        RequestBody requestBody = RequestBody.create(act, JSON);
        Request request = new Request.Builder().url(AppConfig.REGISTER_API).post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String respS = Objects.requireNonNull(response.body()).string();
            Log.i(TAG, "remoteRegister: " + respS);
            BaseResponse respJ = gson.fromJson(respS, BaseResponse.class);
            return respJ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BaseResponse remoteGetAccessTokenByRefreshToken(final String refreshToken) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create("", JSON);
        Request request = new Request.Builder().url(AppConfig.GET_ACCESS_TOKEN_API).addHeader("AuthorizationRefreshToken", refreshToken).post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String respS = Objects.requireNonNull(response.body()).string();
            Log.i(TAG, "remoteGetAccessTokenByRefreshToken: " + respS);
            BaseResponse baseResponse = gson.fromJson(respS, BaseResponse.class);
            return baseResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BaseResponse<List<LoadAllFriendRelationsData>> remoteLoadAllFriendsByToken(String token) {
        OkHttpClient okHttpClient = null;
        okHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create("", JSON);
        Request request = new Request.Builder().url(AppConfig.LOAD_FRIENDS_API).addHeader("AuthorizationAccessToken", token).post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String respS = Objects.requireNonNull(response.body()).string();
            Log.d(TAG, "run: " + respS);
            BaseResponse<List<LoadAllFriendRelationsData>> relations = gson.fromJson(respS, new TypeToken<BaseResponse<List<LoadAllFriendRelationsData>>>() {
            }.getType());
            return relations;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
