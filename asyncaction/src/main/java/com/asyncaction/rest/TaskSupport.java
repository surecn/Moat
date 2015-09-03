package com.asyncaction.rest;

import android.content.Context;

import com.asyncaction.action.Action;
import com.asyncaction.action.ActionFlow;
import com.asyncaction.action.ActionManager;

/**
 * Created by surecn on 15/8/5.
 */
public class TaskSupport {

    /*package*/static ActionFlow createWork(RestAdapter adapter, final MethodInfo methodInfo) {
        return ActionManager.create(createTask(adapter, methodInfo));
    }

    /*package*/static Action createTask(RestAdapter adapter, final MethodInfo methodInfo) {
        return new HttpAction(adapter, methodInfo) {
            @Override
            public void run(Context context, ActionFlow work) throws Exception {
                Object data = HttpBehavior.behavior(mAdapter, mMethodInfo);
                work.onNext(data);
            }
        };
    }

}
