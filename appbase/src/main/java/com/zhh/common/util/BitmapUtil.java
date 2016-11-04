package com.zhh.common.util;

/**
 * Created by MHL on 2016/9/2.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class BitmapUtil {

    // 生成圆角图片
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 14;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    @Deprecated
    // 将Bitmap转换成Base64
    public static String getImgStr(Bitmap bit, int quality) {

        if (bit == null) {
            return null;
        }

        byte[] bytes = getImgBytes(bit, quality);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static byte[] getImgBytes(Bitmap bit, int quality) {

        if (bit == null) {
            return null;
        }

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.JPEG, quality, bos);// 参数100表示不压缩
            return bos.toByteArray();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        if (options == null || reqWidth <= 0 || reqHeight <= 0) {
            return 1;
        }

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromStream(Context context, Uri uri, int reqWidth, int reqHeight) {

        if (context == null || uri == null || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }

        InputStream stream = null;
        try {
            stream = context.getContentResolver().openInputStream(uri);
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // It seems that BitmapFactory closed the stream after decode
            BitmapFactory.decodeStream(stream, null, options);
            close(stream);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            stream = context.getContentResolver().openInputStream(uri);
            // It seems that BitmapFactory closed the stream after decode
            return BitmapFactory.decodeStream(stream, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(stream);
        }

        return null;
    }

    public static Bitmap decodeSampledBitmapFromLocalFile(Context context, File file, int reqWidth, int reqHeight) {

        if (context == null || file == null || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }

        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // It seems that BitmapFactory closed the stream after decode
            BitmapFactory.decodeStream(stream, null, options);
            close(stream);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            stream = new FileInputStream(file);
            // It seems that BitmapFactory closed the stream after decode
            return BitmapFactory.decodeStream(stream, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(stream);
        }

        return null;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap createBitmapInSize(Bitmap bitmap, int reqWidth, int reqHeight) {

        if (bitmap == null || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }

        float reqRate = reqHeight / (float) reqWidth;
        float rate = bitmap.getHeight() / (float) bitmap.getWidth();

        if (rate > reqRate) {
            int height = (int)(bitmap.getWidth() * reqRate);
            int offsetY = (int)((bitmap.getHeight() - height) / 2);
            Matrix matrix = new Matrix();
            float scale = reqWidth / (float)bitmap.getWidth();
            matrix.setScale(scale, scale);
            return Bitmap.createBitmap(bitmap, 0, offsetY, bitmap.getWidth(), height, matrix, false);
        }

        int width = (int)(bitmap.getHeight() / reqRate);
        int offsetX = (int)((bitmap.getWidth() - width) / 2);
        Matrix matrix = new Matrix();
        float scale = reqWidth / (float)width;
        matrix.setScale(scale, scale);
        return Bitmap.createBitmap(bitmap, offsetX, 0, width, bitmap.getHeight(), matrix, false);
    }

    public static Bitmap createBitmapWithRatio(Bitmap bitmap, float ratio) {

        if (bitmap == null || ratio <= 0) {
            return null;
        }

        float reqRate = ratio;
        float rate = bitmap.getHeight() / (float) bitmap.getWidth();

        if (rate > reqRate) {
            int height = (int)(bitmap.getWidth() * reqRate);
            int offsetY = (int)((bitmap.getHeight() - height) / 2);
            return Bitmap.createBitmap(bitmap, 0, offsetY, bitmap.getWidth(), height, null, false);
        }

        int width = (int)(bitmap.getHeight() / reqRate);
        int offsetX = (int)((bitmap.getWidth() - width) / 2);
        return Bitmap.createBitmap(bitmap, offsetX, 0, width, bitmap.getHeight(), null, false);
    }

    public static Bitmap createBitmapWithLimitSize(Bitmap bitmap, int reqWidth, float ratio) {

        if (bitmap == null || reqWidth <= 0 || ratio <= 0) {
            return null;
        }

        float reqRate = ratio;
        float rate = bitmap.getHeight() / (float) bitmap.getWidth();

        if (rate > reqRate) {
            int height = (int)(bitmap.getWidth() * reqRate);
            int offsetY = (int)((bitmap.getHeight() - height) / 2);
            Matrix matrix = new Matrix();
            float scale = reqWidth / (float)bitmap.getWidth();
            if (scale < 1) {
                matrix.setScale(scale, scale);
            } else {
                matrix.setScale(1, 1);
            }
            return Bitmap.createBitmap(bitmap, 0, offsetY, bitmap.getWidth(), height, matrix, false);
        }

        int width = (int)(bitmap.getHeight() / reqRate);
        int offsetX = (int)((bitmap.getWidth() - width) / 2);
        Matrix matrix = new Matrix();
        float scale = reqWidth / (float)width;
        if (scale < 1) {
            matrix.setScale(scale, scale);
        } else {
            matrix.setScale(1, 1);
        }
        return Bitmap.createBitmap(bitmap, offsetX, 0, width, bitmap.getHeight(), matrix, false);
    }

    public static Bitmap decodeBitmapFromStream(Context context, Uri uri, int reqWidth, int reqHeight) {

        if (context == null || uri == null || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }

        Bitmap bitmap = decodeSampledBitmapFromStream(context, uri, reqWidth, reqHeight);
        Bitmap targetBitmap = createBitmapInSize(bitmap, reqWidth, reqHeight);
        if (!bitmap.equals(targetBitmap)) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return targetBitmap;
    }

    public static Bitmap decodeBitmapFromFromResource(Context context, int resId,
                                                      int reqWidth, int reqHeight) {
        if(context == null || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }

        Bitmap bitmap = decodeSampledBitmapFromResource(context.getResources(), resId, reqWidth, reqHeight);
        Bitmap targetBitmap = createBitmapInSize(bitmap, reqWidth, reqHeight);
        if (!bitmap.equals(targetBitmap)) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return targetBitmap;
    }

    public static Bitmap decodeBitmapFromFromLocal(Context context, File file, int reqWidth, int reqHeight) {
        if(context == null || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }

        Bitmap bitmap = decodeSampledBitmapFromLocalFile(context, file, reqWidth, reqHeight);
        if(bitmap == null) {
            return null;
        }
        Bitmap targetBitmap = createBitmapInSize(bitmap, reqWidth, reqHeight);
        if (!bitmap.equals(targetBitmap)) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return targetBitmap;
    }

    public static Bitmap blur(Bitmap bkg, int scale, int radius) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bkg, bkg.getWidth() / scale, bkg.getHeight() / scale, false);
        return FastBlur.doBlur(scaledBitmap, radius / scale, true);
    }

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
