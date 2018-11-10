package com.asyncaction.action;

import android.content.Context;

/**
 * Created by surecn on 15/8/3.
 */
public interface Action {
    public void run(Context context, ActionFlow work) throws Exception;
}
