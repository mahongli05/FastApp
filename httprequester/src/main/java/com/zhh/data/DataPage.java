package com.zhh.data;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MHL on 2016/7/7.
 */
public class DataPage<T> {

    /*
    *   list: [
            { }
        ],
        page: 1,
        total_count: 100,
    * */

    @SerializedName("list")
    public List<T> list;

    @SerializedName("page")
    public int page;

    @SerializedName("size")
    public int size;

    @SerializedName("total_count")
    public int totalCount;
}

