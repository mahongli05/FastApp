package com.zhh.common.util;

import android.widget.TextView;

/**
 * Created by MHL on 2016/9/12.
 */
public class TextUtils {

    public static String getText(TextView textView) {
        try {
            return textView.getText().toString().trim();
        } catch (Exception e) {

        }
        return null;
    }
}
