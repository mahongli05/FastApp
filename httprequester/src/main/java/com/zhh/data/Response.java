package com.zhh.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MHL on 2016/7/7.
 */
public class Response<T> {

    @SerializedName("msg")
    public String msg;

    @SerializedName("status")
    public int status;

    @SerializedName("data")
    public T data;
}
