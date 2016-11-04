package com.zhh.common.http.converter;

import com.zhh.common.http.HttpRequester;
import com.zhh.common.http.HttpRequester.DataConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MHL on 2016/6/30.
 */
public class DataConverterSet implements DataConverter {

    private List<DataConverter> mConverters = new ArrayList<>();

    public void addConverter(DataConverter converter) {
        mConverters.add(converter);
    }

    @Override
    public void convertData(Object data, HttpRequester.RequestResult result) {
        result.data = data;
        for (DataConverter converter : mConverters) {
            Object resultData = result.data;
            if (resultData != null) {
                converter.convertData(result.data.toString(), result);
            }
        }
    }
}
