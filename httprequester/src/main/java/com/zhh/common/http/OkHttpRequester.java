package com.zhh.common.http;

import android.text.TextUtils;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhh.common.util.LogHelper;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Created by MHL on 2016/6/29.
 */
class OkHttpRequester extends HttpRequester {

    private static final String TAG = "OkHttpRequester";

    private static final long CONNECT_TIMEOUT = 45L * 1000;
    private static final long READ_TIMEOUT = 30L * 1000;
    private static final long WRITE_TIMEOUT = 30L * 1000;

    @Override
    public RequestResult execute(Request request) {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        RequestResult result = new RequestResult();

        try {
            com.squareup.okhttp.Request.Builder builder = new com.squareup.okhttp.Request.Builder();

            Set<Map.Entry<String, String>> headers = request.getHeaders().entrySet();
            for (Map.Entry<String, String> entry : headers) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }

            String url = request.getRequestUrl();

            builder.url(url);

            LogHelper.d("API_url", url);

            Request.Method method = request.getMethod();
            if (method == Request.Method.GET) {
                builder.get();
            } else if (method == Request.Method.PUT) {
                builder.put(getRequestBody(request));
            } else if (method == Request.Method.POST) {
                builder.post(getRequestBody(request));
            }

            final Response response = client.newCall(builder.build()).execute();
            if (result != null) {
                result.code = response.code();
                DataConverter converter = request.getDataConverter();
                if (converter != null) {
                    converter.convertData(response.body().bytes(), result);
                } else {
                    result.data = response.body().string();
                }
                result.message = response.message();
            }
        } catch (Exception e) {
            if (result != null) {
                result.exception = e;
            }
            LogHelper.e(TAG, "execute", e);
        }

        return result;
    }

    private RequestBody getRequestBody(Request request) {

        Set<Map.Entry<String, String>> forms = request.getForms().entrySet();
        if (!forms.isEmpty()) {
            FormEncodingBuilder formBuilder = new FormEncodingBuilder();
            for (Map.Entry<String, String> entry : forms) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
            return formBuilder.build();
        }

        String jsonBody = request.getRequestBody();
        if (!TextUtils.isEmpty(jsonBody)) {
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
        }

        return RequestBody.create(null, "");
    }
}
