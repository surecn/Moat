package com.asynchandler.task;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by surecn on 15/8/3.
 */
public class FlowManager {

    public final static int OBSERVER_PRE = 1001;

    public final static int OBSERVER_COMPLETE = 1002;

    public final static int OBSERVER_ERROR = 1003;

    private ErrorInterceptor mErrorInterceptor;

    private Context mContext;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            TaskFlow taskFlow = (TaskFlow) msg.obj;
            final ITaskObserver observer = taskFlow.getObserver();
            switch (msg.what) {
                case OBSERVER_PRE:
                    if (observer != null) {
                        observer.onPre();
                    }
                    mFixedThreadPool.execute(new WorkRunnable(taskFlow, taskFlow.getPriority()));
                    break;
                case OBSERVER_COMPLETE:
                    if (observer != null) {
                        observer.onComplete(taskFlow.getResult());
                        observer.onFinally();
                    }
                    break;
                case OBSERVER_ERROR:
                    if (observer != null) {
                        if (taskFlow.isDefaultErrorHandler() &&
                                mErrorInterceptor != null) {
                            if (!mErrorInterceptor.interceptor(taskFlow.getThrowable())) {
                                observer.onError(taskFlow.getThrowable());
                            }
                        } else {
                            observer.onError(taskFlow.getThrowable());
                        }
                        observer.onFinally();
                    }
                    break;
            }
        }
    };

    private ExecutorService mFixedThreadPool;

    private static FlowManager sWorkFlow;

    public static FlowManager getInstance() {
        if (sWorkFlow == null) {
            sWorkFlow = new FlowManager();
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

    private FlowManager() {
    }

    /**
     * 创建一个任务流
     * @param task 第一个任务
     * @return
     */
    public static TaskFlow create(Task task) {
        return new TaskFlow(getInstance(), task, Thread.NORM_PRIORITY, 0);
    }

    /**
     * 创建一个任务流
     * @param task 第一个任务
     * @param delayMillis 执行延时时间
     * @return
     */
    public static TaskFlow create(Task task, long delayMillis) {
        return new TaskFlow(getInstance(), task, Thread.NORM_PRIORITY, delayMillis);
    }

    /**
     * 创建一个任务流
     * @param task 第一个任务
     * @param priority 任务流的执行优先级
     * @return
     */
    public static TaskFlow create(Task task, int priority) {
        return new TaskFlow(getInstance(), task, priority, 0);
    }

    /**
     * 创建一个任务流
     * @param task 第一个任务
     * @param priority 任务流的执行优先级
     * @param delayMillis 执行延时时间
     * @return
     */
    public static TaskFlow create(Task task, int priority, long delayMillis) {
        return new TaskFlow(getInstance(), task, priority, delayMillis);
    }

    /**
     * 设置错误拦截器
     * @return
     */
    public void setErrorInterceptor(ErrorInterceptor errorInterceptor) {
        this.mErrorInterceptor = errorInterceptor;
    }

    /*package*/void start(TaskFlow work) {
        Message msg = mHandler.obtainMessage(OBSERVER_PRE);
        msg.obj = work;
        msg.sendToTarget();
    }

}
