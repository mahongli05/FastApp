package com.zhh.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zhh.appbase.R;
import com.zhh.control.JsHandler;


/**
 * Created by MHL on 2016/8/10.
 */
public class BrowserActivity extends TopBarActivity {

    private static final String TITLE_SEPARATOR = " - ";

    public static final String EXTRA_URL = "extra_url";

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            String url = intent.getStringExtra(EXTRA_URL);
            mWebView.loadUrl(url);
        }
    }

    private void setupView() {

        mRightText.setText(R.string.refresh);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });

        setupWebView();
    }

    private void setupWebView() {

        View.inflate(this, R.layout.activity_browser, mContentFrame);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setWebViewClient(mViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setDownloadListener(mDownloadListener);
        mWebView.addJavascriptInterface(new JsHandler(this), JsHandler.NAME);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= 7) {
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setDomStorageEnabled(true);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setLightTouchEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir()
                .getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(false);

        mWebView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                return false;
            }
        });
    }

    private final DownloadListener mDownloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, final String userAgent,
                                    final String contentDisposition, final String mimetype,
                                    long contentLength) {

        }
    };

    private WebViewClient mViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            updateTitle(view.getTitle());
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setProgress(newProgress);
        }

        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            updateTitle(title);
        }
    };

    private void updateTitle(String title) {
        String subtitle = getSubtitle(title);
        if (!TextUtils.isEmpty(subtitle)) {
            mMiddleText.setText(subtitle);
        }
    }

    private String getSubtitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            String[] titles = title.split(TITLE_SEPARATOR);
            if (titles.length > 0) {
                return titles[0];
            }
        }
        return title;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (goBack()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean goBack() {
        if (null != mWebView && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }
}
