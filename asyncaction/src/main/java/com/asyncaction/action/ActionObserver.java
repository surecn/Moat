package com.asyncaction.action;

/**
 * Created by surecn on 15/8/3.
 */
public class ActionObserver<T> implements ResultObserver<T>, ProcessObserver {

    /**
     * 第一个任务流执行前
     */
    @Override
    public void onPre() {
    }

    /**
     * TaskFlow调用publishUpdate时调用
     * @param what
     * @param objs
     */
    @Override
    public void onUpdate(int what, Object... objs) {
    }

    /**
     * 任务流正常执行完成时调用
     * @param result
     */
    @Override
    public void onComplete(T result) {
    }

    /**
     *任务流执行出现错误时完调用
     * @param throwable
     */
    @Override
    public void onError(Throwable throwable) {
    }

    /**
     *任务流最后执行
     */
    @Override
    public void onFinally() {
    }
}
