package com.zhh.httprequester;

import com.google.gson.annotations.SerializedName;
import com.zhh.common.http.HttpRequester.RequestResult;
import com.zhh.common.http.converter.JsonDataConverter;
import com.zhh.common.util.JsonUtil;

import java.lang.reflect.Type;

/**
 * Created by MHL on 2016/9/13.
 */
public class TypeIgnoreJsonDataConverter extends JsonDataConverter {

    public TypeIgnoreJsonDataConverter(Class classType) {
        super(classType);
    }

    public TypeIgnoreJsonDataConverter(Type type) {
        super(type);
    }

    @Override
    public void convertData(Object data, RequestResult result) {
        super.convertData(data, result);
        if (result.data == null) {
            convertToSimpleResponse(data, result);
        }
    }

    private void convertToSimpleResponse(Object data, RequestResult result) {
        JsonDataConverter converter = new JsonDataConverter(SimpleResponse.class);
        converter.convertData(data, result);
        if (result.data != null) {
            String json = JsonUtil.toJson(result.data);
            super.convertJsonData(json, result);
        }
    }

    private static class SimpleResponse {

        @SerializedName("msg")
        public String msg;

        @SerializedName("status")
        public int status;
    }
}
