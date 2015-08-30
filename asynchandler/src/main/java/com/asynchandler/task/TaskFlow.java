package com.asynchandler.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surecn on 15/8/3.
 */
public class TaskFlow<R> {

    private List<TaskEntry> mTaskList;

    private ITaskObserver mObserver;

    private FlowManager mFlowManager;

    private Throwable mThrowable;

    private Object mResult;

    private State mState = State.next;

    private boolean mDefaultErrorHandler = true;

    private int mPriority;

    /*package*/enum State{
        next,
        error
    }

    /*package*/TaskFlow(FlowManager flowManager, Task task, int priority, long delayMillis) {
        mFlowManager = flowManager;
        mPriority = priority;
        mTaskList = new ArrayList<TaskEntry>();
        mTaskList.add(new TaskEntry(task, delayMillis));
    }

    /*package*/FlowManager getWorkFlow() {
        return mFlowManager;
    }

    /*package*/List<TaskEntry> getTaskList() {
        return mTaskList;
    }

    /*package*/State getCurrentState() {
        return mState;
    }

    /*package*/void setThrowable(Throwable throwable) {
        mThrowable = throwable;
    }

    /*package*/Throwable getThrowable() {
        return mThrowable;
    }

    /*package*/ITaskObserver getObserver() {
        return mObserver;
    }

    /*package*/Object getResult() {
        return mResult;
    }

    /*package*/boolean isDefaultErrorHandler() {
        return mDefaultErrorHandler;
    }

    /**
     * 任务流增加一个任务
     * @param task
     * @return
     */
    public TaskFlow contact(Task task) {
        return contact(task, 0);
    }

    /**
     * 任务流增加一个任务
     * @param task
     * @param delayMillis 改任务执行时的延时时间，单位毫秒
     * @return
     */
    public TaskFlow contact(Task task, long delayMillis) {
        mTaskList.add(new TaskEntry(task, delayMillis));
        return this;
    }

    /**
     * 设置监听,设置后回立即执行任务流
     * @param observer
     */
    public void setObserver(ITaskObserver observer){
        mObserver = observer;
        mFlowManager.start(this);
    }

    /**
     * 执行任务流
     */
    public void start() {
        mFlowManager.start(this);
    }

    /**
     * 设置该任务执行成功结果
     * @param result
     */
    public void onNext(R result) {
        mState = State.next;
        mResult = result;
    }

    /**
     * 设置该任务执行错误的异常
     * @param throwable
     */
    public void onError(Throwable throwable) {
        mState = State.error;
    }

    /**
     * 设置该任务流是否调用统一错误处理
     * @param flag
     * @return
     */
    public TaskFlow setDefaultErrorHandler(boolean flag) {
        mDefaultErrorHandler = flag;
        return this;
    }

    /**
     * 该任务优先级
     * @return
     */
    public int getPriority() {
        return mPriority;
    }
}
