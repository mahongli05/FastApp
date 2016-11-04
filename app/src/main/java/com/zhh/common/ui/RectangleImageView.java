package com.zhh.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.mhl.fastapp.R;

/**
 * Created by MHL on 2016/10/13.
 */
public class RectangleImageView extends ImageView {

    private float mAspectRatio = 1;

    public RectangleImageView(Context context) {
        this(context, null);
    }

    public RectangleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.custom);
        mAspectRatio = ta.getFloat(R.styleable.custom_radio, 1f);
        ta.recycle();
    }

    public void setAspectRatio(float ratio) {
        mAspectRatio = ratio;
        requestLayout();
    }

    public float getAspectRatio() {
        return mAspectRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int)(width * mAspectRatio));
    }
}
