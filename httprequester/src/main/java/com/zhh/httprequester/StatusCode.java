package com.zhh.httprequester;

/**
 * Created by MHL on 2016/9/1.
 */
public class StatusCode {

    /*
    *  This is our status code, change to yourself
    * */

    /*
        OK = 0

        UNKNOWN_ERROR = 1
        HTTPS_REQUIRED = 2
        PARAM_REQUIRED = 3
        DATA_ERROR = 4
        PARAM_ERROR = 5

        USER_AUTHENTICATION_ERROR = 9
        USER_NOT_ACTIVATED = 10
        USER_REGISTERED = 11
        USER_NOT_EXIST = 12
        USER_ALREADY_ACTIVE = 13
        ACTIVATION_CODE_INVALID = 14
        ACTIVATION_CODE_EXPIRED = 15
        AUTH_ERROR = 16
        TOKEN_INVALID = 17
        TOKEN_VALIDATION_FAIL = 18

        CONNECT_AUTH_ERROR = 20
        USER_CANCEL_CONNECT = 21
        USER_CANCEL_REGISTER = 22
    * */

    public static final int STATUS_OK = 0;

    public static final int STATUS_UNKNOWN_ERROR = 1;
    public static final int STATUS_HTTPS_REQUIRED = 2;
    public static final int STATUS_PARAM_REQUIRED = 3;
    public static final int STATUS_DATA_ERROR = 4; // 所有的token失效，账户被ti都返回这个！
    public static final int STATUS_PARAM_ERROR = 5;

    public static final int STATUS_USER_AUTHENTICATION_ERROR = 9;
    public static final int STATUS_USER_NOT_ACTIVATED = 10;
    public static final int STATUS_USER_REGISTERED = 11;
    public static final int STATUS_USER_NOT_EXIST = 12;
    public static final int STATUS_USER_ALREADY_ACTIVE = 13;
    public static final int STATUS_ACTIVATION_CODE_INVALID = 14;
    public static final int STATUS_ACTIVATION_CODE_EXPIRED = 15;
    public static final int STATUS_AUTH_ERROR = 16;
    public static final int STATUS_TOKEN_INVALID = 17;
    public static final int STATUS_TOKEN_VALIDATION_FAIL = 18;

    public static final int STATUS_CONNECT_AUTH_ERROR = 20;
    public static final int STATUS_USER_CANCEL_CONNECT = 21;
    public static final int STATUS_USER_CANCEL_REGISTER = 22;
}

