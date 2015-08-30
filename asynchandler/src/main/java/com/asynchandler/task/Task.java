package com.asynchandler.task;

import android.content.Context;

/**
 * Created by surecn on 15/8/3.
 */
public interface Task {
    public void run(Context context, TaskFlow work) throws Exception;
}
