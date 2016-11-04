package com.zhh.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by MHL on 2016/9/21.
 */
public class NetworkUtil {

    public static  boolean isNetworkConnected(Context context) {
        if(context == null)
            return false;
        ConnectivityManager nw = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if(nw == null)
            return false;
        NetworkInfo netinfo = nw.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected()) {
            return true;
        }

        return false;
    }
}
