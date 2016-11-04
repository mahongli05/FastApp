package com.zhh.common.http;

/**
 * Created by MHL on 2016/7/7.
 */
public class HttpCode {

    /*
    *   400	请求参数有误
    *   401 用户未登录
        403	用户未登录
        404	资源未找到
        405	请求方法不支持
        500	服务器错误
    * */

    public static final int STATUS_OK = 200;
    public static final int STATUS_400 = 400;
    public static final int STATUS_401 = 401;
    public static final int STATUS_403 = 403;
    public static final int STATUS_404 = 404;
    public static final int STATUS_405 = 405;
    public static final int STATUS_500 = 500;
}
