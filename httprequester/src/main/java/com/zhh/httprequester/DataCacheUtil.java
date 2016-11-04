package com.zhh.httprequester;

import android.content.Context;
import android.text.TextUtils;

import com.zhh.common.util.FileUtil;

import java.io.File;

/**
 * Created by MHL on 2016/10/11.
 */
public class DataCacheUtil {

    private static final String CACHE_ROOT_DIR = "http_requester_cache";
    private static final String PUBLIC_CACHE_DIR = "public";
    private static String sPublicCacheDirPath;

    public static String getPublicCacheFile(Context context, String fileName) {
        if (TextUtils.isEmpty(sPublicCacheDirPath)) {
            StringBuilder builder = new StringBuilder();
            sPublicCacheDirPath = builder.append(context.getCacheDir())
                    .append(File.separator)
                    .append(CACHE_ROOT_DIR)
                    .append(File.separator)
                    .append(PUBLIC_CACHE_DIR)
                    .append(File.separator)
                    .toString();
        }
        return sPublicCacheDirPath + fileName;
    }

    public static String getPersonalCacheFile(Context context, String userId, String fileName) {
            StringBuilder builder = new StringBuilder();
            return builder.append(context.getCacheDir())
                    .append(File.separator)
                    .append(CACHE_ROOT_DIR)
                    .append(File.separator)
                    .append(userId)
                    .append(File.separator)
                    .append(fileName)
                    .toString();
    }

    public static void saveToFile(String filePath, String content) {
        File file = new File(filePath);
        FileUtil.ensureFile(file);
        FileUtil.writeFile(file, content);
    }

    public static String readFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return FileUtil.readFile(file);
        }
        return null;
    }
}
