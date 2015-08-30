package com.asynchandler.task;

import android.os.Handler;
import android.os.Message;

import java.util.List;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-30
 * Time: 16:02
 */
/*package*/class WorkRunnable implements Runnable, Comparable<WorkRunnable> {

    private TaskFlow mTaskFlow;

    private int mPriority;

    /*package*/WorkRunnable(TaskFlow taskFlow, int priority) {
        mTaskFlow = taskFlow;
        mPriority = priority;
    }

    /*package*/int getPriority() {
        return mPriority;
    }

    @Override
    public void run() {
        Handler handler = FlowManager.getInstance().getHandler();
        final List<TaskEntry> taskList = mTaskFlow.getTaskList();
        try {
            for (int i = 0, len = taskList.size(); i < len; i++) {
                if (mTaskFlow.getCurrentState() == TaskFlow.State.next) {
                    TaskEntry entry = (TaskEntry) taskList.get(i);
                    long delay = entry.getDelayMillis();
                    if (delay > 0) {
                        Thread.sleep(delay);
                    }
                    entry.getTask().run(mTaskFlow.getWorkFlow().getContext(), mTaskFlow);
                } else {
                    break;
                }
            }
            if (mTaskFlow.getCurrentState() == TaskFlow.State.error) {
                Message msg = handler.obtainMessage(FlowManager.OBSERVER_ERROR);
                msg.obj = mTaskFlow;
                msg.sendToTarget();
            } else if (mTaskFlow.getCurrentState() == TaskFlow.State.next) {
                Message msg = handler.obtainMessage(FlowManager.OBSERVER_COMPLETE);
                msg.obj = mTaskFlow;
                msg.sendToTarget();
            } else {
                Message msg = handler.obtainMessage(FlowManager.OBSERVER_FINALLY);
                msg.obj = mTaskFlow;
                msg.sendToTarget();
            }
        } catch (Exception e) {
            Message msg = handler.obtainMessage(FlowManager.OBSERVER_ERROR);
            mTaskFlow.setThrowable(e);
            msg.obj = mTaskFlow;
            msg.sendToTarget();
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(WorkRunnable another) {
        if (this.getPriority() < another.getPriority()) {
            return 1;
        }
        if (this.getPriority() > another.getPriority()) {
            return -1;
        }
        return 0;
    }
};