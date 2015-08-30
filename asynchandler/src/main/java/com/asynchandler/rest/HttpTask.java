package com.asynchandler.rest;

import android.content.Context;

import com.asynchandler.rest.interceptor.RequestInterceptor;
import com.asynchandler.rest.interceptor.ResponseInterceptor;
import com.asynchandler.task.Task;
import com.asynchandler.task.TaskFlow;

/**
 * Created by surecn on 15/8/4.
 * 在TaskFlow中执行
 */
/*package*/class HttpTask implements Task {

    protected MethodInfo mMethodInfo;

    protected RestAdapter mAdapter;

    public HttpTask(RestAdapter adapter, MethodInfo methodInfo) {
        mMethodInfo = methodInfo;
        mAdapter = adapter;
    }

    @Override
    public void run(Context context, TaskFlow work) throws Exception {
    }
}
