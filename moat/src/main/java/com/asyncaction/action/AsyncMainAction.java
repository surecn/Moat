package com.asyncaction.action;

import android.content.Context;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-31
 * Time: 09:48
 */
public interface AsyncMainAction {
    public void run(Context context, ActionFlow work) throws Exception;
}
