package com.zhh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.zhh.appbase.R;


/**
 * Created by MHL on 2016/9/19.
 */
public class CustomProgressDialog extends Dialog {

    private TextView mMessageView;
    private CharSequence mMessage;

    public CustomProgressDialog(Context context) {
        super(context, R.style.Custom_Progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_progress);
        mMessageView = (TextView) findViewById(R.id.message);
        mMessageView.setText(mMessage);
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(mMessage);
        }
    }
}
