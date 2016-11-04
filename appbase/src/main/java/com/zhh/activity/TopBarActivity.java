package com.zhh.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhh.appbase.R;

/**
 * Created by MHL on 2016/9/1.
 */
public class TopBarActivity extends BaseActivity {

    protected View mLeftView;
    protected TextView mMiddleText;
    protected View mRightView;
    protected TextView mRightText;
    protected ImageView mRightImage;
    protected FrameLayout mContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_bar);
        setupTopBar();
    }

    protected void setupTopBar() {

        mLeftView = findViewById(R.id.left_image);
        mMiddleText = (TextView) findViewById(R.id.middle_text);
        mRightView = findViewById(R.id.right_container);
        mRightText = (TextView) findViewById(R.id.right_text);
        mRightImage = (ImageView) findViewById(R.id.right_image);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);

        mLeftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftClick();
            }
        });

        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick();
            }
        });
    }

    protected void onLeftClick() {
        finish();
    }

    protected void onRightClick() {

    }

    protected void fillContent(View content) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentFrame.addView(content, params);
    }

    protected void fillContent(int contentId) {
        View.inflate(this, contentId, mContentFrame);
    }
}
