package com.asyncaction.action;

import android.os.Handler;
import android.os.Message;

import java.util.LinkedList;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-30
 * Time: 16:02
 */
/*package*/class ConcurrentRunnable implements Runnable, Comparable<ConcurrentRunnable> {

    private ConcurrentActionPool mActionPool;

    private int mPriority;

    /*package*/ConcurrentRunnable(ConcurrentActionPool actionPool, int priority) {
        mActionPool = actionPool;
        mPriority = priority;
    }

    /*package*/int getPriority() {
        return mPriority;
    }

    @Override
    public void run() {
        final Handler handler = ActionManager.getInstance().getHandler();
        final LinkedList<ActionEntry> actionList = mActionPool.getTaskList();
        while (true) {
            ActionEntry entry = null;
            synchronized (actionList) {
                entry = actionList.poll();
            }
            if (entry == null) {
                break;
            }
            try {
                final long delay = entry.getDelayMillis();
                final Object task = entry.getTask();
                if (task instanceof Action) {
                    if (delay > 0) {
                        Thread.sleep(delay);
                    }
                    Action action = (Action) entry.getTask();
                    action.run(mActionPool.getActionManager().getContext(), mActionPool);
                }
            } catch (Exception e) {
                Message msg = handler.obtainMessage(ActionManager.OBSERVER_ERROR);
                mActionPool.setThrowable(e);
                msg.obj = mActionPool;
                msg.sendToTarget();
                e.printStackTrace();
            }
        }
        ActionFlow.State state = mActionPool.getCurrentState();
        switch (state) {
            case error:{
                Message msg = handler.obtainMessage(ActionManager.OBSERVER_ERROR);
                msg.obj = mActionPool;
                msg.sendToTarget();
                break;}
            case next:{
                Message msg = handler.obtainMessage(ActionManager.OBSERVER_COMPLETE);
                msg.obj = mActionPool;
                msg.sendToTarget();
                break;}
            default:{
                Message msg = handler.obtainMessage(ActionManager.OBSERVER_FINALLY);
                msg.obj = mActionPool;
                msg.sendToTarget();
                break;}
        }
    }

    @Override
    public int compareTo(ConcurrentRunnable another) {
        if (this.getPriority() < another.getPriority()) {
            return 1;
        }
        if (this.getPriority() > another.getPriority()) {
            return -1;
        }
        return 0;
    }
};