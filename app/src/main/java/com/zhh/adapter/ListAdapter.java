package com.zhh.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MHL on 2016/9/14.
 */
public class ListAdapter<T> extends BaseAdapter {

    protected List<T> mListItems = new ArrayList<>();

    public void setData(List<T> data) {
        mListItems.clear();
        if (data != null) {
            mListItems.addAll(data);
        }
        notifyDataSetChanged();
    }

    public List getData() {
        return mListItems;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
