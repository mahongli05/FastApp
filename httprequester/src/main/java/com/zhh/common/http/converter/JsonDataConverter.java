package com.zhh.common.http.converter;

import com.zhh.common.http.HttpRequester.DataConverter;
import com.zhh.common.http.HttpRequester.RequestResult;
import com.zhh.common.util.JsonUtil;
import com.zhh.common.util.LogHelper;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class JsonDataConverter implements DataConverter {

    private Class mClassType;
    private Type mType;

    public JsonDataConverter(Class classType) {
        mClassType = classType;
    }

    public JsonDataConverter(Type type) {
        mType = type;
    }

    @Override
    public void convertData(Object data, RequestResult result) {

        String jsonData = null;
        if (data instanceof String) {
            jsonData = (String) data;
        } else if (data instanceof byte[]) {
            jsonData = new String((byte[])data, Charset.forName("UTF-8"));
        }

        LogHelper.d("API_data", jsonData);

        convertJsonData(jsonData, result);
    }

    protected void convertJsonData(String jsonData, RequestResult result) {

        if (jsonData != null) {
            try {
                if (mClassType != null) {
                    result.data = JsonUtil.toObject(mClassType, jsonData);
                } else if (mType != null) {
                    result.data = JsonUtil.toObject(mType, jsonData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}