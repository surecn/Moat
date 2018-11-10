package com.asyncaction.rest;

import android.content.Context;

import com.asyncaction.action.Action;
import com.asyncaction.action.ActionFlow;

/**
 * Created by surecn on 15/8/4.
 * 在TaskFlow中执行
 */
/*package*/class HttpAction implements Action {

    protected MethodInfo mMethodInfo;

    protected RestAdapter mAdapter;

    public HttpAction(RestAdapter adapter, MethodInfo methodInfo) {
        mMethodInfo = methodInfo;
        mAdapter = adapter;
    }

    @Override
    public void run(Context context, ActionFlow work) throws Exception {
    }
}
