package com.zhh.httprequester;

import android.content.Context;
import android.text.TextUtils;

import com.zhh.common.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MHL on 2016/9/12.
 */
public class ParamsBuilder {

    private static final String TAG = "ParamsBuilder";

    /*
    *   imei	true	string	手机IMEI号
        anid	true	string	手机Android ID
        did	    true	string	手机Android ID的md5值
        gaid	true	string	google advertiser id
        ov	true	string	手机OS version
        lc	true	string	国家语言代码，如zh_CN
        mcc	true	string	移动国家代码(Mobile Country Code)
        nt	true	int	    手机network 2G:1,3G:2,4G:3,wifi:4
        pm	false	int	    price model，CPI:1,CPC:2,CPA:4,VIDEO:8,CPE:16
        page	false	int	页码，从1开始
        count	false	int	每页大小
        vc  true  int client version code
    * */

    private static final String IMEI = "imei";
    private static final String ANID = "anid";
    private static final String DID = "did";
    private static final String GAID = "gaid";
    private static final String OV = "ov";
    private static final String LC = "region";
    private static final String MCC = "mcc";
    private static final String NT = "nt";
    private static final String PM = "pm";
    private static final String PAGE = "page";
    private static final String COUNT = "count";
    private static final String VC = "vc";

    private Map<String, String> mParams;
    private Context mContext;

    public ParamsBuilder(Context context) {
        mContext = context;
        mParams = new HashMap<>();
    }

    public ParamsBuilder token(String token) {
        mParams.put("token", token);
        return this;
    }

    public ParamsBuilder addAll(Map<String, String> params) {
        if (params != null) {
            mParams.putAll(params);
        }
        return this;
    }

    public ParamsBuilder versionCode(int version) {
        mParams.put(VC, String.valueOf(version));
        return this;
    }

    public ParamsBuilder androidId(String androidId) {
        if (TextUtils.isEmpty(androidId)) {
            androidId = ParamsConfig.getAndroidId(mContext);
        }
        mParams.put(ANID, androidId);
        return this;
    }

    public ParamsBuilder androidIdMd5(String androidIdMd5) {
        if (TextUtils.isEmpty(androidIdMd5)) {
            androidIdMd5 = ParamsConfig.getAndroidIdMd5(mContext);
        }
        mParams.put(DID, androidIdMd5);
        return this;
    }

    public ParamsBuilder osVersion(String osVersion) {
        if (TextUtils.isEmpty(osVersion)) {
            osVersion = ParamsConfig.getOsVersion();
        }
        mParams.put(OV, osVersion);
        return this;
    }

    public ParamsBuilder languageCode(String languageCode) {
        if (TextUtils.isEmpty(languageCode)) {
            languageCode = ParamsConfig.getLanguageCode(mContext);
        }
        mParams.put(LC, languageCode);
        return this;
    }

    public ParamsBuilder mcc(String mcc) {
        if (TextUtils.isEmpty(mcc)) {
            mcc = ParamsConfig.getMcc(mContext);
        }
        mParams.put(MCC, mcc);
        return this;
    }

    public ParamsBuilder networkType(String networkType) {
        if (TextUtils.isEmpty(networkType)) {
            networkType = ParamsConfig.getNetworkType(mContext);
        }
        mParams.put(NT, networkType);
        return this;
    }

    public ParamsBuilder priceModel(int priceModel) {
        mParams.put(PM, String.valueOf(priceModel));
        return this;
    }

    public ParamsBuilder page(int page) {
        mParams.put(PAGE, String.valueOf(page));
        return this;
    }

    public ParamsBuilder count(int count) {
        mParams.put(COUNT, String.valueOf(count));
        return this;
    }

    public ParamsBuilder add(String key, String value) {
        mParams.put(key, value);
        return this;
    }

    public ParamsBuilder randomParams() {
        int[] randomParams = generateRandomParams();
        mParams.put("a", String.valueOf(randomParams[0]));
        mParams.put("b", String.valueOf(randomParams[1]));
        mParams.put("c", String.valueOf(randomParams[2]));
        return this;
    }

    private static int[] generateRandomParams() {
        long time = System.currentTimeMillis();
        int a = (int)(time & 0x0fffffff);
        int b = (int)(Math.random() * 1000 + 1);
        int c = a % b;
        int[] result = new int[3];
        result[0] = a;
        result[1] = b;
        result[2] = c;
        return result;
    }

    public String buildJson() {
        return JsonUtil.toJson(mParams);
    }

    public Map<String, String> getParams() {
        return mParams;
    }
}
