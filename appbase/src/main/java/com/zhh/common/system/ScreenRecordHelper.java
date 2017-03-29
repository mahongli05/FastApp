package com.zhh.common.system;

/**
 * Created by MHL on 2017/3/29.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.IOException;

/**
 * Created by MHL on 2017/3/27.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecordHelper {

    private static final int PERMISSION_CODE = 1;
    private static final int DISPLAY_WIDTH = 480;
    private static final int DISPLAY_HEIGHT = 640;

    public static final String RECORD_PATH = "/sdcard/capture.mp4";

    private MediaProjectionManager mProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaRecorder mMediaRecorder;

    private Activity mActivity;
    private boolean mIsRecording;

    public ScreenRecordHelper(Activity activity) {
        mActivity = activity;
        mIsRecording = false;
    }

    public void requestRecord() {
        if (mProjectionManager == null) {
            mProjectionManager = (MediaProjectionManager)
                    mActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }
        mActivity.startActivityForResult(mProjectionManager.createScreenCaptureIntent(), PERMISSION_CODE);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PERMISSION_CODE) {
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        stopRecord();
        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        startRecord();
    }

    private void startRecord() {
        if (mMediaProjection != null) {
            mMediaProjection.registerCallback(mMediaProjectionCallback, null);
            mMediaRecorder = new MediaRecorder();
            initRecorder();
            prepareRecorder();
            mVirtualDisplay = createVirtualDisplay();
            mMediaRecorder.start();
            mIsRecording = true;
        }
    }

    private void initRecorder() {
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        mMediaRecorder.setOutputFile(RECORD_PATH);
    }

    private VirtualDisplay createVirtualDisplay() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return mMediaProjection.createVirtualDisplay("MainActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT,  metrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null /*Handler*/);
    }

    private void prepareRecorder() {
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        if (mMediaProjection != null) {
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }
        mIsRecording = false;
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    private MediaProjection.Callback mMediaProjectionCallback = new MediaProjection.Callback() {

        @Override
        public void onStop() {
            stopRecord();
        }

    };
}

