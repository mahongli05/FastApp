package com.zhh.common.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.example.mhl.fastapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MHL on 2016/5/25.
 */
public class UnLimitSlidePager extends FrameLayout {

    private ViewPager mViewPager;
    private PageIndicator mPageIndicator;
    private ImageView mMirrorView;
    private Bitmap mMirrorBitmap;
    private UnLimitSlideAdapter mUnLimitAdapter = new UnLimitSlideAdapter();
    private List<ViewPager.OnPageChangeListener> mOnPageChangeListeners = new ArrayList<>();

    public UnLimitSlidePager(Context context) {
        this(context, null);
    }

    public UnLimitSlidePager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnLimitSlidePager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupView();
    }

    private void setupView() {

        inflate(getContext(), R.layout.view_unlimit_slide_page, this);
        mMirrorView = new ImageView(getContext());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

                if (mUnLimitAdapter.getCount() == 1) {
                    for (ViewPager.OnPageChangeListener listener : mOnPageChangeListeners) {
                        listener.onPageScrolled(i, v, i1);
                    }
                    return;
                }

                if (i == mUnLimitAdapter.getCount() - 2 && v > 0.99f) {
                    mViewPager.setCurrentItem(0, false);
                } else if (i == mUnLimitAdapter.getCount() - 1) {
                    mViewPager.setCurrentItem(0, false);
                } else {
                    for (ViewPager.OnPageChangeListener listener : mOnPageChangeListeners) {
                        listener.onPageScrolled(i, v, i1);
                    }
                }
            }

            @Override
            public void onPageSelected(int i) {

                int index = i;
                if (mUnLimitAdapter.getCount() != 1
                        && i == mUnLimitAdapter.getCount() - 1) {
                    index = 0;
                }

                for (ViewPager.OnPageChangeListener listener : mOnPageChangeListeners) {
                    listener.onPageSelected(index);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                for (ViewPager.OnPageChangeListener listener : mOnPageChangeListeners) {
                    listener.onPageScrollStateChanged(i);
                }
            }
        });

        mPageIndicator = (PageIndicator) findViewById(R.id.indicator);
        ((CirclePageIndicator) mPageIndicator).setFillColor(getResources()
                .getColor(R.color.color_blue));
        ((CirclePageIndicator) mPageIndicator).setPageColor(getResources()
                .getColor(R.color.color_white));
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (item == 0) {
            mViewPager.setCurrentItem(mUnLimitAdapter.getCount() - 1, smoothScroll);
        } else {
            mViewPager.setCurrentItem(item, smoothScroll);
        }
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mOnPageChangeListeners.add(listener);
    }

    public void setPagerAdapter(PagerAdapter adapter) {
        mUnLimitAdapter.setSrcAdapter(adapter);
        mViewPager.setAdapter(mUnLimitAdapter);
        mPageIndicator.setViewPager(mViewPager);
    }

    public class UnLimitSlideAdapter extends PagerAdapter {

        private PagerAdapter mSrcAdapter;
        private View mFirstView;

        public void setSrcAdapter(PagerAdapter adapter) {
            if (mSrcAdapter != null) {
                mSrcAdapter.unregisterDataSetObserver(mDataSetObserver);
            }
            mSrcAdapter = adapter;
            mSrcAdapter.registerDataSetObserver(mDataSetObserver);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {

            if (mSrcAdapter == null) {
                return 0;
            }

            int count = mSrcAdapter.getCount();
            if (count == 1) {
                return 1;
            }

            return count + 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            if (mSrcAdapter.getCount() == 1) {
                mSrcAdapter.destroyItem(container, position, object);
                return;
            }

            if (mSrcAdapter != null && position < mSrcAdapter.getCount()) {
                mSrcAdapter.destroyItem(container, position, object);
            } else {
                container.removeView(mMirrorView);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (mSrcAdapter.getCount() == 1) {
                return mSrcAdapter.instantiateItem(container, position);
            }

            if (mSrcAdapter != null && position < mSrcAdapter.getCount()) {
                Object obj = mSrcAdapter.instantiateItem(container, position);
                if (position == 0 && obj instanceof View) {
                    mFirstView = (View) obj;
                }
                return obj;
            } else if (mFirstView != null) {
                boolean enable = mFirstView.isDrawingCacheEnabled();
                mFirstView.setDrawingCacheEnabled(true);
                Bitmap bitmap = mFirstView.getDrawingCache();
                if (bitmap != null) {
                    Bitmap oldBitmap = mMirrorBitmap;
                    mMirrorBitmap = Bitmap.createBitmap(bitmap);
                    mMirrorView.setImageBitmap(mMirrorBitmap);
                    if (oldBitmap != null) {
                        oldBitmap.recycle();
                    }
                    ViewParent parent = mMirrorView.getParent();
                    if (parent instanceof ViewGroup) {
                        ((ViewGroup) parent).removeView(mMirrorView);
                    }
                    container.addView(mMirrorView);
                    mFirstView.setDrawingCacheEnabled(enable);
                }
                return mMirrorView;
            }
            return null;
        }

        private DataSetObserver mDataSetObserver = new DataSetObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                notifyDataSetChanged();
            }
        };
    }
}
