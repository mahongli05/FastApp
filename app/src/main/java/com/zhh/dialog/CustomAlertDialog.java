package com.zhh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mhl.fastapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MHL on 2016/10/13.
 */
public class CustomAlertDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.tips)
    TextView mTips;
    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.confirm)
    TextView mConfirm;
    @BindView(R.id.dialog_container)
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
        ButterKnife.bind(this, this);
        setupView();
    }

    private void setupView() {
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
