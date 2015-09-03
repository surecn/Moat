package com.asyncaction.action;

import android.os.Handler;
import android.os.Message;

import java.util.List;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-30
 * Time: 16:02
 */
/*package*/class FlowRunnable implements Runnable, Comparable<FlowRunnable> {

    private ActionFlow mActionFlow;

    private int mPriority;

    /*package*/FlowRunnable(ActionFlow actionFlow, int priority) {
        mActionFlow = actionFlow;
        mPriority = priority;
    }

    /*package*/int getPriority() {
        return mPriority;
    }

    @Override
    public void run() {
        Handler handler = ActionManager.getInstance().getHandler();
        final List<ActionEntry> taskList = mActionFlow.getTaskList();
        try {
            for (int i = 0, len = taskList.size(); i < len; i++) {
                if (mActionFlow.getCurrentState() == ActionFlow.State.next) {
                    ActionEntry entry = (ActionEntry) taskList.get(i);
                    long delay = entry.getDelayMillis();
                    Object task = entry.getTask();
                    if (task instanceof Action) {
                        if (delay > 0) {
                            Thread.sleep(delay);
                        }
                        ((Action) task).run(mActionFlow.getActionManager().getContext(), mActionFlow);
                    } else if (task instanceof MainAction) {
                        Message msg = handler.obtainMessage(ActionManager.OBSERVER_MAINTASK);
                        msg.obj = mActionFlow;
                        msg.arg1 = i;
                        msg.setTarget(handler);
                        handler.sendMessageDelayed(msg, delay);
                        synchronized (mActionFlow) {
                            mActionFlow.wait();
                        }
                    } else if (task instanceof AsyncMainAction) {
                        Message msg = handler.obtainMessage(ActionManager.OBSERVER_MAINTASK);
                        msg.obj = mActionFlow;
                        msg.arg1 = i;
                        msg.setTarget(handler);
                        handler.sendMessageDelayed(msg, delay);
                    }
                } else {
                    break;
                }
            }
            ActionFlow.State state = mActionFlow.getCurrentState();
            switch (state) {
                case error:{
                    Message msg = handler.obtainMessage(ActionManager.OBSERVER_ERROR);
                    msg.obj = mActionFlow;
                    msg.sendToTarget();
                    break;}
                case next:{
                    Message msg = handler.obtainMessage(ActionManager.OBSERVER_COMPLETE);
                    msg.obj = mActionFlow;
                    msg.sendToTarget();
                    break;}
                default:{
                    Message msg = handler.obtainMessage(ActionManager.OBSERVER_FINALLY);
                    msg.obj = mActionFlow;
                    msg.sendToTarget();
                    break;}
            }
        } catch (Exception e) {
            Message msg = handler.obtainMessage(ActionManager.OBSERVER_ERROR);
            mActionFlow.setThrowable(e);
            msg.obj = mActionFlow;
            msg.sendToTarget();
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(FlowRunnable another) {
        if (this.getPriority() < another.getPriority()) {
            return 1;
        }
        if (this.getPriority() > another.getPriority()) {
            return -1;
        }
        return 0;
    }
};