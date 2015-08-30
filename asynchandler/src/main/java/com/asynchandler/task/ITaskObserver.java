package com.asynchandler.task;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-30
 * Time: 14:02
 */
public interface ITaskObserver<T> {
    /**
     * 第一个任务流执行前
     */
    public void onPre();

    /**
     * 任务流正常执行完成时调用
     * @param result
     */
    public void onComplete(T result);

    /**
     *任务流执行出现错误时完调用
     * @param throwable
     */
    public void onError(Throwable throwable);

    /**
     *任务流最后执行
     */
    public void onFinally();
}
