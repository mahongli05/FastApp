package com.zhh.common.http.converter;

/**
 * Created by MHL on 2016/6/30.
 */
public class AesJsonConverter extends DataConverterSet {

    public AesJsonConverter(Class type, String key) {
        addConverter(new AesConverter(key));
        addConverter(new JsonDataConverter(type));
    }
}
