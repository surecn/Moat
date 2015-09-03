package com.asyncaction.action;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by surecn on 15/8/3.
 */
public class ActionManager {

    /*package*/final static int FLOW_PRE = 1001;

    /*package*/final static int OBSERVER_COMPLETE = 1002;

    /*package*/final static int OBSERVER_ERROR = 1003;

    /*package*/final static int OBSERVER_FINALLY = 1004;

    /*package*/final static int OBSERVER_MAINTASK = 1005;

    /*package*/final static int OBSERVER_ASYNCMAINTASK = 1006;

    /*package*/final static int CONCURRENT_POOL_PRE = 1007;

    private ErrorInterceptor mErrorInterceptor;

    private Context mContext;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLOW_PRE:{
                    ActionFlow actionFlow = (ActionFlow) msg.obj;
                    executePre(actionFlow);
                    mFixedThreadPool.execute(new FlowRunnable(actionFlow, actionFlow.getPriority()));
                    break;}
                case OBSERVER_COMPLETE:{
                    ActionFlow actionFlow = (ActionFlow) msg.obj;
                    executeComplete(actionFlow);
                    executeFinally(actionFlow);
                    break;}
                case OBSERVER_ERROR:{
                    ActionFlow actionFlow = (ActionFlow) msg.obj;
                    executeError(actionFlow);
                    executeFinally(actionFlow);
                    break;}
                case OBSERVER_FINALLY:{
                    ActionFlow actionFlow = (ActionFlow) msg.obj;
                    final ProcessObserver observer = actionFlow.getProcessObserver();
                    observer.onFinally();
                    break;}
                case OBSERVER_MAINTASK:{
                    ActionFlow actionFlow = (ActionFlow) msg.obj;
                    executeMainTask(actionFlow, msg.arg1);
                    break;}
                case OBSERVER_ASYNCMAINTASK:{
                    ActionFlow actionFlow = (ActionFlow) msg.obj;
                    executeAsyncMainTask(actionFlow, msg.arg1);
                    break;}
                case CONCURRENT_POOL_PRE:{
                    ConcurrentActionPool actionFlow = (ConcurrentActionPool) msg.obj;
                    executePre(actionFlow);
                    int length = msg.arg1;
                    if (length <= 0) {
                        length = 0;
                    }
                    for (int i = 0; i < length; i++) {
                        mFixedThreadPool.execute(new FlowRunnable(actionFlow, actionFlow.getPriority()));
                    }
                    break;}
                default:
                    break;
            }
        }
    };

    private void executeAsyncMainTask(ActionFlow actionFlow, int index) {
        Object obj = actionFlow.getTaskList().get(index);
        if (obj instanceof AsyncMainAction) {
            try {
                ((AsyncMainAction) obj).run(mContext, actionFlow);
            } catch (Exception e) {
                e.printStackTrace();
                Message errorMsg = mHandler.obtainMessage(OBSERVER_ERROR);
                errorMsg.obj = actionFlow;
                errorMsg.sendToTarget();
            }
        }
    }

    private void executeMainTask(ActionFlow actionFlow, int index) {
        Object obj = actionFlow.getTaskList().get(index);
        if (obj instanceof MainAction) {
            try {
                ((MainAction) obj).run(mContext, actionFlow);
                synchronized (actionFlow) {
                    actionFlow.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message errorMsg = mHandler.obtainMessage(OBSERVER_ERROR);
                errorMsg.obj = actionFlow;
                errorMsg.sendToTarget();
            }
        }
    }

    private void executePre(ActionFlow actionFlow) {
        final ProcessObserver observer = actionFlow.getProcessObserver();
        if (observer != null) {
            observer.onPre();
        }
    }

    private void executeComplete(ActionFlow actionFlow) {
        final ResultObserver observer = actionFlow.getTaskObserver();
        if (observer != null) {
            observer.onComplete(actionFlow.getResult());
        }
    }

    private void executeFinally(ActionFlow actionFlow) {
        final ProcessObserver observer = actionFlow.getProcessObserver();
        if (observer != null) {
            observer.onFinally();
        }
    }

    private void executeError(ActionFlow actionFlow) {
        final ResultObserver observer = actionFlow.getTaskObserver();
        if (observer != null) {
            if (actionFlow.isDefaultErrorHandler() &&
                    mErrorInterceptor != null) {
                if (!mErrorInterceptor.interceptor(actionFlow.getThrowable())) {
                    observer.onError(actionFlow.getThrowable());
                }
            } else {
                observer.onError(actionFlow.getThrowable());
            }
        }
    }

    private ExecutorService mFixedThreadPool;

    private static ActionManager sWorkFlow;

    public static ActionManager getInstance() {
        if (sWorkFlow == null) {
            sWorkFlow = new ActionManager();
        }
        return sWorkFlow;
    }

    /*package*/Handler getHandler() {
        return mHandler;
    }

    /*package*/Context getContext() {
        return mContext;
    }

    /**
     * FlowManager初始化,线程数缺省下为3个
     * @param context
     */
    public void init(Context context) {
        init(context, 3);
    }

    /**
     * FlowManager初始化
     * @param context
     * @param threadCount 线程数
     */
    public void init(Context context, int threadCount) {
        mContext = context;
        mFixedThreadPool = new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.SECONDS,new PriorityBlockingQueue<Runnable>());
    }

    private ActionManager() {
    }

    /**
     * 创建一个任务流
     * @param action 第一个任务
     * @return
     */
    public static ActionFlow create(Action action) {
        return new ActionFlow(getInstance(), action, Thread.NORM_PRIORITY, 0);
    }

    /**
     * 创建一个任务流
     * @param action 第一个任务
     * @param delayMillis 执行延时时间
     * @return
     */
    public static ActionFlow create(Action action, long delayMillis) {
        return new ActionFlow(getInstance(), action, Thread.NORM_PRIORITY, delayMillis);
    }

    /**
     * 创建一个任务流
     * @param action 第一个任务
     * @param priority 任务流的执行优先级
     * @return
     */
    public static ActionFlow create(Action action, int priority) {
        return new ActionFlow(getInstance(), action, priority, 0);
    }

    /**
     * 创建一个任务流
     * @param action 第一个任务
     * @param priority 任务流的执行优先级
     * @param delayMillis 执行延时时间
     * @return
     */
    public static ActionFlow create(Action action, int priority, long delayMillis) {
        return new ActionFlow(getInstance(), action, priority, delayMillis);
    }

    /**
     * 设置错误拦截器
     * @return
     */
    public void setErrorInterceptor(ErrorInterceptor errorInterceptor) {
        this.mErrorInterceptor = errorInterceptor;
    }

    /*package*/void start(ActionFlow flow) {
        Message msg = mHandler.obtainMessage(FLOW_PRE);
        msg.obj = flow;
        msg.sendToTarget();
    }

    /*package*/void start(ConcurrentActionPool pool, int conCurrentCount) {
        Message msg = mHandler.obtainMessage(CONCURRENT_POOL_PRE);
        msg.obj = pool;
        msg.arg1 = conCurrentCount;
        msg.sendToTarget();
    }

}
