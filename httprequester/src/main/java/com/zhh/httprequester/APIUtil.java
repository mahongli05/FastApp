package com.zhh.httprequester;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.zhh.common.http.HttpCode;
import com.zhh.common.http.HttpRequester;
import com.zhh.common.http.HttpUtil;
import com.zhh.common.util.JsonUtil;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * Created by MHL on 2016/10/14.
 */
public class APIUtil {

    public static HttpRequester.RequestResult getString(String url) {

        HttpRequester.Request request = new HttpRequester.Request();
        request.setUrl(url);
        request.setMethod(HttpRequester.Request.Method.GET);
        request.setDataConverter(null);

        HttpRequester requester = HttpUtil.getHttpRequester();
        return requester.execute(request);
    }

    static HttpRequester.RequestResult post(String url, Map<String, String> body, Type type) {

        HttpRequester.Request request = new HttpRequester.Request();
        request.setUrl(url);
        request.setMethod(HttpRequester.Request.Method.POST);
        request.setDataConverter(new TypeIgnoreJsonDataConverter(type));
        request.addForms(body);

        HttpRequester requester = HttpUtil.getHttpRequester();
        return requester.execute(request);
    }

    static HttpRequester.RequestResult get(String url, Map<String, String> params, Type type) {

        HttpRequester.Request request = new HttpRequester.Request();
        request.setUrl(url);
        request.setMethod(HttpRequester.Request.Method.GET);
        request.setDataConverter(new TypeIgnoreJsonDataConverter(type));
        request.addParams(params);

        HttpRequester requester = HttpUtil.getHttpRequester();
        return requester.execute(request);
    }

    static HttpRequester.RequestResult getWithCacheEnable(Context context, String url, String id,
                 Map<String, String> params, Type type) {

        HttpRequester.Request request = new HttpRequester.Request();
        request.setUrl(url);
        request.setMethod(HttpRequester.Request.Method.GET);
        request.setDataConverter(new TypeIgnoreJsonDataConverter(type));
        request.addParams(params);

        HttpRequester requester = HttpUtil.getHttpRequester();
        HttpRequester.RequestResult result = requester.execute(request);

        int httpCode = ResultUtil.getHttpCode(result);

        String filePath = null;
        if (params != null && params.containsKey("token")) {
            if (!TextUtils.isEmpty(id)) {
                params.remove("token");
                String requestUrl = buildRequestUrl(url, params);
                String fileName = String.valueOf(requestUrl.hashCode());
                filePath = DataCacheUtil.getPersonalCacheFile(context, id, fileName);
            }
        } else {
            String requestUrl = request.getRequestUrl();
            String fileName = String.valueOf(requestUrl.hashCode());
            filePath = DataCacheUtil.getPublicCacheFile(context, fileName);
        }

        if (!TextUtils.isEmpty(filePath)) {
            if (httpCode != HttpCode.STATUS_OK) {
                String content = DataCacheUtil.readFromFile(filePath);
                result.data = JsonUtil.toObject(type, content);
            } else if (ResultUtil.isResultOk(result)) {
                String content = JsonUtil.toJson(result.data);
                DataCacheUtil.saveToFile(filePath, content);
            }
        }

        return result;
    }

    static String buildRequestUrl(String baseUrl, Map<String, String> params) {

        Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
        if (!params.isEmpty()) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.appendQueryParameter(key, value);
            }
        }

        return builder.build().toString();
    }
}
