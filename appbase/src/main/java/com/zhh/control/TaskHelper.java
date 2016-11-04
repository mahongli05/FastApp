package com.zhh.control;

import android.os.AsyncTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MHL on 2016/10/9.
 */
public class TaskHelper {

    private Map<Object, AsyncTask> mAsyncTaskMap = new HashMap<>();

    public void execute(AsyncTask<Void, Void, ?> task) {
        Object key = task.getClass();
        cancel(task.getClass());
        mAsyncTaskMap.put(key, task);
        task.execute();
    }

    public void execute(AsyncTask<Void, Void, ?> task, Object key) {
        cancel(key);
        mAsyncTaskMap.put(key, task);
        task.execute();
    }

    public void cancel(Object key) {
        AsyncTask oldTask = mAsyncTaskMap.get(key);
        if (oldTask != null) {
            oldTask.cancel(true);
        }
    }

    public void cancelAllTask() {
        Collection<AsyncTask> tasks = mAsyncTaskMap.values();
        for (AsyncTask task : tasks) {
            task.cancel(true);
        }
        mAsyncTaskMap.clear();
    }
}
