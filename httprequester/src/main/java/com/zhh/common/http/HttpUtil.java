package com.zhh.common.http;

import com.zhh.common.http.converter.JsonDataConverter;

/**
 * Created by MHL on 2016/6/29.
 */
public class HttpUtil {

    private static final String AES_KEY = "httprequester###";

    public static HttpRequester getHttpRequester() {
        return new OkHttpRequester();
    }

    public static HttpRequester.DataConverter getDataConverter(Class type) {
        return new JsonDataConverter(type);
    }
}
