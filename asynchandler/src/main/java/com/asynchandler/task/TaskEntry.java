package com.asynchandler.task;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-30
 * Time: 12:08
 */
/*package*/class TaskEntry {

    private Task mTask;

    private long mDelayMillis;

    /*package*/TaskEntry(Task task, long delayMillis) {
        mTask = task;
        mDelayMillis = delayMillis;
    }

    /*package*/Task getTask() {
        return mTask;
    }

    /*package*/long getDelayMillis() {
        return mDelayMillis;
    }

}