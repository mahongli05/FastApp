package com.zhh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhh.appbase.R;

/**
 * Created by MHL on 2016/10/13.
 */
public class CustomAlertDialog extends Dialog implements View.OnClickListener {

    TextView mTitle;
    TextView mTips;
    TextView mCancel;
    TextView mConfirm;
    LinearLayout mDialogContainer;
    private AlertButtonListener mAlertButtonListener;

    private String mTitleText;
    private String mMessageText;
    private String mLeftText;
    private String mRightText;

    public void setTitleText(String titleText) {
        mTitleText = titleText;
        if (mTitle != null) {
            mTitle.setText(mTitleText);
        }
    }

    public void setMessageText(String messageText) {
        mMessageText = messageText;
        if (mTips != null) {
            mTips.setText(mMessageText);
        }
    }

    public void setLeftText(String leftText) {
        mLeftText = leftText;
        if (mCancel != null) {
            mCancel.setText(mLeftText);
        }
    }

    public void setRightText(String rightText) {
        mRightText = rightText;
        if (mConfirm != null) {
            mConfirm.setText(mRightText);
        }
    }

    public CustomAlertDialog(Context context) {
        super(context, R.style.CustomNormalDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setupView();
    }

    private void setupView() {

        mTitle = (TextView) findViewById(R.id.title);
        mTips = (TextView) findViewById(R.id.tips);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mCancel = (TextView) findViewById(R.id.cancel);
        mDialogContainer = (LinearLayout) findViewById(R.id.dialog_container);

        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mDialogContainer.setOnClickListener(this);
        setTitle(mTitleText);
        setMessageText(mMessageText);
        setLeftText(mLeftText);
        setRightText(mRightText);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v == mConfirm) {
            if (mAlertButtonListener != null) {
                mAlertButtonListener.onRightButtonClick();
            }
        } else if (v == mCancel) {
            if (mAlertButtonListener != null) {
                mAlertButtonListener.onLeftButtonClick();
            }
        }
    }

    public void setAlertButtonListener(AlertButtonListener listener) {
        mAlertButtonListener = listener;
    }

    public interface AlertButtonListener {
        void onLeftButtonClick();

        void onRightButtonClick();
    }
}
