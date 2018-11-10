package com.asyncaction.action;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-30
 * Time: 12:08
 */
/*package*/class ActionEntry {

    private Object mTask;

    private long mDelayMillis;

    /*package*/ActionEntry(Object task, long delayMillis) {
        mTask = task;
        mDelayMillis = delayMillis;
    }

    /*package*/Object getTask() {
        return mTask;
    }

    /*package*/long getDelayMillis() {
        return mDelayMillis;
    }

}