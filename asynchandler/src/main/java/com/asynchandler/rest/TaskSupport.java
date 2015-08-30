package com.asynchandler.rest;

import android.content.Context;

import com.asynchandler.rest.interceptor.RequestInterceptor;
import com.asynchandler.rest.interceptor.ResponseInterceptor;
import com.asynchandler.task.TaskFlow;
import com.asynchandler.task.FlowManager;

/**
 * Created by surecn on 15/8/5.
 */
public class TaskSupport {

    /*package*/static TaskFlow createWork(RestAdapter adapter, final MethodInfo methodInfo) {
        return FlowManager.create(new HttpTask(adapter, methodInfo) {
            @Override
            public void run(Context context, TaskFlow work) throws Exception {
                Object data = HttpBehavior.behavior(mAdapter, mMethodInfo);
                work.onNext(data);
            }
        });
    }

}
