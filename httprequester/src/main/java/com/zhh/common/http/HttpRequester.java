package com.zhh.common.http;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public abstract class HttpRequester {

    public static class RequestResult {
        public int code;
        public String message;
        public Exception exception;
        public Object data;
    }

    public interface DataConverter {
        void convertData(Object data, RequestResult result);
    }

    public abstract RequestResult execute(Request request);

    public static class Request {

        private Method mMethod = Method.GET;
        private String mUrl;
        private Map<String, String> mHeaders = new HashMap<>();
        private Map<String, String> mForms = new HashMap<>();
        private Map<String, String> mParams = new HashMap<>();
        private DataConverter mConverter;
        private String mRequestBody;

        public void setMethod(Method method) {
            mMethod = method;
        }

        public Method getMethod() {
            return mMethod;
        }

        public void setUrl(String url) {
            mUrl = url;
        }

        public String getRequestUrl() {

            return getRequestUri().toString();
        }

        public Uri getRequestUri() {

            Uri.Builder builder = Uri.parse(mUrl).buildUpon();
            if (!mParams.isEmpty()) {
                Set<Entry<String, String>> entries = mParams.entrySet();
                for (Entry<String, String> entry : entries) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.appendQueryParameter(key, value);
                }
            }

            return builder.build();
        }

        public void addHeader(String key, String value) {
            mHeaders.put(key, value);
        }

        public void addHeaders(Map<String, String> headers) {
            if (headers != null) {
                mHeaders.putAll(headers);
            }
        }

        public Map<String, String> getHeaders() {
            return mHeaders;
        }

        public void addForm(String key, String value) {
            mForms.put(key, value);
        }

        public void addForms(Map<String, String> forms) {
            if (forms != null) {
                mForms.putAll(forms);
            }
        }

        public Map<String, String> getForms() {
            return mForms;
        }

        public void addParams(Map<String, String> params) {
            if (params != null) {
                mParams.putAll(params);
            }
        }

        public void addParam(String key, String value) {
            mParams.put(key, value);
        }

        public Map<String, String> getParams() {
            return mParams;
        }

        public void setDataConverter(DataConverter conventer) {
            mConverter = conventer;
        }

        public DataConverter getDataConverter() {
            return mConverter;
        }

        public void setRequestBody(String requestBody) {
            mRequestBody = requestBody;
        }

        public String getRequestBody() {
            return mRequestBody;
        }

        public enum Method {
            GET, POST, PUT
        }
    }
}
