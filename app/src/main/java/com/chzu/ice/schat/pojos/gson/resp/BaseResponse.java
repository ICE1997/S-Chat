package com.chzu.ice.schat.pojos.gson.resp;

public class BaseResponse<T> {
    public String code;
    public String msg;
    public T data;
}
