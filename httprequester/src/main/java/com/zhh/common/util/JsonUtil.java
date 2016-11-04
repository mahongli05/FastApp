package com.zhh.common.util;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by MHL on 2016/7/1.
 */
public class JsonUtil {

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Object toObject(Class type, String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public static Object toObject(Type typeOfT, String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, typeOfT);
    }

    public static String toJson(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }
}
