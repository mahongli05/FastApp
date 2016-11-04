package com.zhh.httprequester;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.zhh.common.util.ByteUtil;
import com.zhh.common.util.LogHelper;
import com.zhh.common.util.SecurityUtil;

/**
 * Created by MHL on 2016/9/12.
 */
public class ParamsConfig {

    private static final String TAG = "ParamsConfig";

    private static String sAndroidId;
    private static String sAndroidIdMd5;
    private static String sOsVersion;
    private static String sLanguageCode;
    private static String sMcc;

    public static String getAndroidId(Context context) {
        if (TextUtils.isEmpty(sAndroidId)) {
            sAndroidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return sAndroidId;
    }

    public static String getAndroidIdMd5(Context context) {

        if (TextUtils.isEmpty(sAndroidIdMd5)) {
            String androidId = getAndroidId(context);
            try {
                byte[] md5bytes = SecurityUtil.getDigest(androidId, "MD5");
                sAndroidIdMd5 = ByteUtil.byte2hex(md5bytes);
            } catch (Exception e) {
                LogHelper.d(TAG, "androidIdMd5", e);
            }
        }
        return sAndroidIdMd5;
    }

    public static String getOsVersion() {
        if (TextUtils.isEmpty(sOsVersion)) {
            sOsVersion = String.valueOf(Build.VERSION.RELEASE);
        }
        return sOsVersion;
    }

    public static String getLanguageCode(Context context) {
        if (TextUtils.isEmpty(sLanguageCode)) {
            sLanguageCode = context.getResources().getConfiguration().locale.toString();
        }
        return sLanguageCode;
    }

    public static String getMcc(Context context) {
        if (TextUtils.isEmpty(sMcc)) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            String operator = telephonyManager.getNetworkOperator();
            if (TextUtils.isEmpty(operator)) {
                operator = telephonyManager.getSimOperator();
            }
            if (!TextUtils.isEmpty(operator) && operator.length() > 3) {
                sMcc = operator.substring(0, 3);
            }
        }
        if (TextUtils.isEmpty(sMcc)) {
            return "000";
        }
        return sMcc;
    }

    // 手机network 2G:1,3G:2,4G:3,wifi:4

    public static final int NT_TYPE_NONE = 0;
    public static final int NT_TYPE_2G = 1;
    public static final int NT_TYPE_3G = 2;
    public static final int NT_TYPE_4G = 3;
    public static final int NT_TYPE_WIFI = 4;

    public static String getNetworkType(Context context) {
        int strNetworkType = NT_TYPE_NONE;

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = NT_TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by
                        // 11
                        strNetworkType = NT_TYPE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
                        // 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by
                        // 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by
                        // 15
                        strNetworkType = NT_TYPE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by
                        // 13
                        strNetworkType = NT_TYPE_4G;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = NT_TYPE_3G;
                        }

                        break;
                }
            }
        }

        return String.valueOf(strNetworkType);
    }
}
