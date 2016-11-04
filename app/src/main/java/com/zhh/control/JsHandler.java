package com.zhh.control;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by MHL on 2016/9/22.
 */


public class JsHandler {

    public static final String NAME = "live";

    private Context mContext;

    public JsHandler(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void log(String tag, String log) {
        
    }
}
