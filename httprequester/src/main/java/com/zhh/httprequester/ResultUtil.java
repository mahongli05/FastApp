package com.zhh.httprequester;

import com.zhh.common.http.HttpRequester;
import com.zhh.common.http.HttpRequester.RequestResult;
import com.zhh.data.Response;

/**
 * Created by MHL on 2016/9/1.
 */
public class ResultUtil {

    public static boolean isResultOk(HttpRequester.RequestResult requestResult) {

        if (requestResult == null) {
            return false;
        }

        if (requestResult.data instanceof Response) {
            Response response = (Response) requestResult.data;
            return response.status == StatusCode.STATUS_OK;
        }

        return false;
    }

    public static String getResponseMessage(HttpRequester.RequestResult requestResult) {

        if (requestResult == null) {
            return null;
        }

        if (requestResult.data instanceof Response) {
            Response response = (Response) requestResult.data;
            return response.msg;
        }

        return null;
    }

    public static Object getResponseData(HttpRequester.RequestResult requestResult) {

        try {
            if (requestResult == null) {
                return 0;
            }

            if (requestResult.data instanceof Response) {
                Response response = (Response) requestResult.data;
                return response.data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getResponsStatus(HttpRequester.RequestResult requestResult) {

        if (requestResult == null) {
            return 0;
        }

        if (requestResult.data instanceof Response) {
            Response response = (Response) requestResult.data;
            return response.status;
        }

        return StatusCode.STATUS_UNKNOWN_ERROR;
    }

    public static String getResponseString(HttpRequester.RequestResult requestResult) {

        if (requestResult == null) {
            return null;
        }

        if (requestResult.data instanceof String) {
            return (String) requestResult.data;
        }

        return null;
    }

    public static boolean setResponseData(HttpRequester.RequestResult requestResult, Object object) {

        if (requestResult == null) {
            return false;
        }

        if (requestResult.data instanceof Response) {
            Response response = (Response) requestResult.data;
            response.data = object;
            return true;
        }

        return false;
    }

    public static int getHttpCode(HttpRequester.RequestResult requestResult) {

        if (requestResult == null) {
            return 0;
        }

        return requestResult.code;
    }

    public static int getMessageCode(RequestResult requestResult) {

        if (requestResult == null) {
            return StatusCode.STATUS_UNKNOWN_ERROR;
        }

        if (requestResult.data instanceof Response) {
            Response response = (Response) requestResult.data;
            return response.status;
        }

        return StatusCode.STATUS_UNKNOWN_ERROR;
    }

  /*  public static String getResponseMessage(Context context, RequestResult result, int defaultResId) {

        int resId = defaultResId;

        switch (ResultUtil.getMessageCode(result)) {
            case StatusCode.STATUS_UNKNOWN_ERROR:
                resId = R.string.error_unknown;
                break;

            case StatusCode.STATUS_USER_NOT_ACTIVATED:
                resId = R.string.user_not_activate;
                break;

            case StatusCode.STATUS_USER_AUTHENTICATION_ERROR:
                resId = R.string.user_authentication_fail;
                break;

            case StatusCode.STATUS_USER_REGISTERED:
                resId = R.string.user_registered;
                break;

            case StatusCode.STATUS_USER_NOT_EXIST:
                resId = R.string.user_not_exit;
                break;

            case StatusCode.STATUS_USER_ALREADY_ACTIVE:
                resId = R.string.user_already_active;
                break;

            case StatusCode.STATUS_ACTIVATION_CODE_INVALID:
                resId = R.string.activation_code_invalid;
                break;

            case StatusCode.STATUS_ACTIVATION_CODE_EXPIRED:
                resId = R.string.activation_code_expired;
                break;

            case StatusCode.STATUS_AUTH_ERROR:
                resId = R.string.auth_error;
                break;

            case StatusCode.STATUS_TOKEN_INVALID:
                resId = R.string.token_invalid;
                break;

            case StatusCode.STATUS_TOKEN_VALIDATION_FAIL:
                resId = R.string.token_validation_fail;
                break;
        }

        return context.getResources().getString(resId);
    }*/

    public static boolean isTokenInvalid(RequestResult result) {
        int code = getMessageCode(result);
        return code == StatusCode.STATUS_TOKEN_INVALID
                || code == StatusCode.STATUS_TOKEN_VALIDATION_FAIL;
    }
}

