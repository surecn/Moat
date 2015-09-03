package com.asyncaction.action;

import java.util.LinkedList;

/**
 * Created by surecn on 15/8/3.
 */
public class ActionFlow<R> {

    private final static int CONCURRENT_COUNT = 3;

    private LinkedList<ActionEntry> mTaskList;

    private ResultObserver mTaskObserver;

    private ProcessObserver mProcessObserver;

    private ActionManager mActionManager;

    private Throwable mThrowable;

    private Object mResult;

    private State mState = State.next;

    private boolean mDefaultErrorHandler = true;

    private int mPriority;

    /*package*/enum State{
        next,
        error,
        cancel
    }

    /*package*/ActionFlow(ActionManager actionManager, Action action, int priority, long delayMillis) {
        mActionManager = actionManager;
        mPriority = priority;
        mTaskList = new LinkedList<ActionEntry>();
        mTaskList.add(new ActionEntry(action, delayMillis));
    }

    /*package*/ActionManager getActionManager() {
        return mActionManager;
    }

    /*package*/LinkedList<ActionEntry> getTaskList() {
        return mTaskList;
    }

    /*package*/synchronized State getCurrentState() {
        return mState;
    }

    /*package*/void setThrowable(Throwable throwable) {
        mThrowable = throwable;
    }

    /*package*/Throwable getThrowable() {
        return mThrowable;
    }

    /*package*/ResultObserver getTaskObserver() {
        return mTaskObserver;
    }

    /*package*/ProcessObserver getProcessObserver() {
        return mProcessObserver;
    }

    /*package*/Object getResult() {
        return mResult;
    }

    /*package*/boolean isDefaultErrorHandler() {
        return mDefaultErrorHandler;
    }

    /**
     * 任务流增加一个任务
     * @param action
     * @return
     */
    public ActionFlow add(Action action) {
        return add(action, 0);
    }

    /**
     * 任务流增加一个任务
     * @param action
     * @return
     */
    public ActionFlow add(Action action, long delayMillis) {
        mTaskList.add(new ActionEntry(action, delayMillis));
        return this;
    }

    /**
     * 任务流增加一个主线程任务
     * @param task
     * @return
     */
    public ActionFlow add(MainAction task) {
        return add(task, 0);
    }

    /**
     * 任务流增加一个主线程任务
     * @param task
     * @return
     */
    public ActionFlow add(MainAction task, long delayMillis) {
        mTaskList.add(new ActionEntry(task, delayMillis));
        return this;
    }

    /**
     * 任务流增加一个异步主线程任务
     * @param task
     * @return
     */
    public ActionFlow add(AsyncMainAction task) {
        return add(task, 0);
    }

    /**
     * 任务流增加一个异步主线程任务
     * @param task
     * @return
     */
    public ActionFlow add(AsyncMainAction task, long delayMillis) {
        mTaskList.add(new ActionEntry(task, delayMillis));
        return this;
    }

    /**
     * 设置监听,设置后回立即执行任务流,一个任务流只能有一个Observer
     * @param observer
     */
    public void setObserver(ResultObserver observer){
        mTaskObserver = observer;
        if (observer instanceof ProcessObserver) {
            mProcessObserver = (ProcessObserver) observer;
        }
        mActionManager.start(this);
    }

    /**
     * 设置监听,设置后回立即执行任务流,一个任务流只能有一个Observer
     * @param taskObserver
     */
    public void setObserver(ResultObserver taskObserver, ProcessObserver processObserver){
        mTaskObserver = taskObserver;
        mProcessObserver = processObserver;
        mActionManager.start(this);
    }

    /**
     * 执行任务流
     */
    public void start() {
        mActionManager.start(this);
    }

    public synchronized void cancel() {
        mState = State.cancel;
    }

    /**
     * 设置该任务执行成功结果
     * @param result
     */
    public synchronized void onNext(R result) {
        mState = State.next;
        mResult = result;
    }

    /**
     * 设置该任务执行错误的异常
     * @param throwable
     */
    public synchronized void onError(Throwable throwable) {
        mState = State.error;
    }

    /**
     * 设置该任务流是否调用统一错误处理
     * @param flag
     * @return
     */
    public ActionFlow setDefaultErrorHandler(boolean flag) {
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
