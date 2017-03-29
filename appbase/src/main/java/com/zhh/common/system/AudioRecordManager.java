package com.zhh.common.system;

/**
 * Created by MHL on 2017/3/29.
 */

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.Semaphore;

/**
 * Created by MHL on 2017/3/22.
 */

public class AudioRecordManager {

    private static final String TAG = "AudioRecord";

    private static final int SAMPLE_RATE_IN_HZ = 8000;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);

    private static final int MSG_START = 1;
    private static final int MSG_STOP = 2;
    private static final int MSG_RECORD = 3;
    private static final int MSG_UPDATE = 4;

    private short[] mBuffer;
    private AudioRecord mAudioRecord;
    private HandlerThread mHandlerThread;
    private Handler mUiHandler;
    private AudioRecordHandler mRecordHandler;
    private Semaphore mSemaphore;

    private static final AudioRecordManager sInstance = new AudioRecordManager();

    private AudioRecordManager() {
        mUiHandler = new AudioUpdateHandler(Looper.getMainLooper());
        mSemaphore = new Semaphore(1);
    }

    public static AudioRecordManager getInstance() {
        return sInstance;
    }

    public void start() {
        mUiHandler.sendEmptyMessage(MSG_START);
    }

    public void stop() {
        mUiHandler.sendEmptyMessage(MSG_STOP);
    }

    private class AudioUpdateHandler extends Handler {

        AudioUpdateHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE) {
                handleUpdate((double)msg.obj);
            } else if (msg.what == MSG_START) {
                handleStart();
            } else if (msg.what == MSG_STOP) {
                handleStop();
            }
        }
    }

    private void handleUpdate(double level) {
        Log.e(TAG, String.format("audio level: %s", level));
    }

    private void handleStart() {

        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("audio record");
            mHandlerThread.start();
            mRecordHandler = new AudioRecordHandler(mHandlerThread.getLooper());
        }

        if (mAudioRecord == null) {
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
            mBuffer = new short[BUFFER_SIZE];
            mAudioRecord.startRecording();
        }

        mRecordHandler.removeMessages(MSG_RECORD);
        mRecordHandler.sendEmptyMessageDelayed(MSG_RECORD, 100);
    }

    private void handleStop() {
        if (mHandlerThread != null) {
            try {
                mSemaphore.acquire();

                mRecordHandler.removeCallbacksAndMessages(null);
                mRecordHandler = null;
                mHandlerThread.quit();
                mHandlerThread = null;
                releaseAudioRecord();

                mSemaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void releaseAudioRecord() {
        if (mAudioRecord != null) {
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
            mBuffer = null;
        }
    }

    private class AudioRecordHandler extends Handler {

        AudioRecordHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RECORD:
                    handleNoiseRecord();
                    break;
            }
        }
    }

    private void handleNoiseRecord() {
        try {
            mSemaphore.acquire();
            if (mAudioRecord != null && mBuffer != null) {
                //r是实际读取的数据长度，一般而言r会小于buffersize
                int r = mAudioRecord.read(mBuffer, 0, BUFFER_SIZE);
                long v = 0;
                // 将 buffer 内容取出，进行平方和运算
                for (int i = 0; i < mBuffer.length; i++) {
                    v += mBuffer[i] * mBuffer[i];
                }
                // 平方和除以数据总长度，得到音量大小。
                double mean = v / (double) r;
                double volume = 10 * Math.log10(mean);
                Log.d(TAG, "分贝值:" + volume);
                Message message = mUiHandler.obtainMessage(MSG_UPDATE, volume);
                mUiHandler.sendMessage(message);
                if (mRecordHandler != null) {
                    mRecordHandler.sendEmptyMessageDelayed(MSG_RECORD, 100);
                }
            }
            mSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


