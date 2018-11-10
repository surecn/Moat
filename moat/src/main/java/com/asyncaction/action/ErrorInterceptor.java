package com.asyncaction.action;

/**
 * Created by surecn on 15/8/5.
 * TaskFlow执行时统一错误处理
 */
public interface ErrorInterceptor {
    public boolean interceptor(Throwable throwable);
}
