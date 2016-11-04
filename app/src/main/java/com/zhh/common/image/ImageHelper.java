package com.zhh.common.image;

/*
*  简单封装image load的接口，采用类似picasso和glide的接口方式.
* */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import com.zhh.common.util.BitmapUtil;

import java.io.File;

/**
 * Created by MHL on 2016/6/30.
 */

public class ImageHelper {

    private static final boolean SHOW_CACHE = false;

    public static LoaderWrapper load(Context context, String path) {
        return new LoaderWrapper(context, path);
    }

    public static LoaderWrapper load(Context context, File file) {
        return new LoaderWrapper(context, file);
    }

    public static LoaderWrapper load(Context context, Uri uri) {
        return new LoaderWrapper(context, uri);
    }

    public static LoaderWrapper load(Context context, int resourceId) {
        return new LoaderWrapper(context, resourceId);
    }

    public static void cancelRequest(Context context, ImageView imageView) {
        Picasso.with(context).cancelRequest(imageView);
    }

    public interface BitmapTransformation {
        Bitmap transform(Bitmap source);
        String key();
    }

    public static class LoaderWrapper {

        RequestCreator mCreator;

        LoaderWrapper(Context context, String path) {
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(SHOW_CACHE);
            mCreator = picasso.load(path);
        }

        LoaderWrapper(Context context, int resourceId) {
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(SHOW_CACHE);
            mCreator = picasso.load(resourceId);
        }

        LoaderWrapper(Context context, File file) {
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(SHOW_CACHE);
            mCreator = picasso.load(file);
        }

        LoaderWrapper(Context context, Uri uri) {
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(SHOW_CACHE);
            mCreator = picasso.load(uri);
        }

        public void into(ImageView imageView) {
            mCreator.into(imageView);
        }

        public LoaderWrapper transform(final BitmapTransformation transformation) {
            mCreator.transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    return transformation.transform(source);
                }

                @Override
                public String key() {
                    return transformation.key();
                }
            });
            return this;
        }

        public LoaderWrapper placeHolder(int resId) {
            mCreator.placeholder(resId);
            return this;
        }

        public LoaderWrapper placeHolder(Drawable drawable) {
            mCreator.placeholder(drawable);
            return this;
        }

        public LoaderWrapper error(int resId) {
            mCreator.error(resId);
            return this;
        }

        public LoaderWrapper error(Drawable drawable) {
            mCreator.error(drawable);
            return this;
        }

        public LoaderWrapper resize(int w, int h) {
            mCreator.resize(w, h);
            return this;
        }

        public LoaderWrapper fetch() {
            mCreator.fetch();
            return this;
        }
    }

    public static class RoundTransformation implements BitmapTransformation {

        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap result = BitmapUtil.getRoundedCornerBitmap(source);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "round" + hashCode();
        }
    }

    public static class FitWidthTransformation implements BitmapTransformation {

        int width;

        public FitWidthTransformation(int width) {
            this.width = width;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int targetHeight = source.getHeight() * width / source.getWidth();
            Bitmap result = Bitmap.createScaledBitmap(source, width, targetHeight, false);
            if (result != null) {
                source.recycle();
                return result;
            }
            return source;
        }

        @Override
        public String key() {
            return "fitwidth" + width;
        }
    }

    public static class FitRatioTransformation implements BitmapTransformation {

        float radio;

        public FitRatioTransformation(float radio) {
            this.radio = radio;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap bitmap = BitmapUtil.createBitmapWithRatio(source, radio);
            if (bitmap != null) {
                source.recycle();
                return bitmap;
            }
            return source;
        }

        @Override
        public String key() {
            return "fitRatio" + radio;
        }
    }

    public static class FitRatioWithXLimitTransformation implements BitmapTransformation {

        int width;
        float radio;

        public FitRatioWithXLimitTransformation(int width, float radio) {
            this.width = width;
            this.radio = radio;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap bitmap = BitmapUtil.createBitmapWithLimitSize(source, width, radio);
            if (bitmap != null) {
                source.recycle();
                return bitmap;
            }
            return source;
        }

        @Override
        public String key() {
            return "fitRatioWithXLimit" + width + radio;
        }
    }

    public static class FitXYTransformation implements BitmapTransformation {

        int width;
        int height;

        public FitXYTransformation(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap bitmap = BitmapUtil.createBitmapInSize(source, width, height);
            if (bitmap != null) {
                source.recycle();
                return bitmap;
            }
            return source;
        }

        @Override
        public String key() {
            return "fixXY" + width;
        }
    }

    public static class BlurTransformation implements BitmapTransformation {

        private int mScale;
        private int mRadius;

        public BlurTransformation(int scale, int radius) {
            mScale = scale;
            mRadius = radius;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap bitmap = BitmapUtil.blur(source, mScale, mRadius);
            if (bitmap != null) {
                source.recycle();
                return bitmap;
            }
            return source;
        }

        @Override
        public String key() {
            return "blur";
        }
    }
}
