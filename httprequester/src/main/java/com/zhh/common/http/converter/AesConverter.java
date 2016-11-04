package com.zhh.common.http.converter;

import com.zhh.common.http.HttpRequester.RequestResult;
import com.zhh.common.http.HttpRequester.DataConverter;
import com.zhh.common.util.AESUtil;

/**
 * Created by MHL on 2016/6/30.
 */
public class AesConverter implements DataConverter {

    private String mKey;

    AesConverter(String key) {
        mKey = key;
    }

    @Override
    public void convertData(Object data, RequestResult result) {
        if (result != null) {
            if (data instanceof byte[]) {
                result.data = AESUtil.decrypt((byte[])data, mKey);
            } else if (data instanceof String) {
                result.data = AESUtil.decrypt((String)data, mKey);
            }
        }
    }
}
