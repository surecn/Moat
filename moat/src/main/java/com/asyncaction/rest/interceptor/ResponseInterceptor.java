package com.asyncaction.rest.interceptor;

import com.asyncaction.rest.IHttpResult;

import java.lang.reflect.Type;

/**
 * Created by surecn on 15/8/5.
 */
public interface ResponseInterceptor {
    public Object intercept(IHttpResult result, Type t) throws Exception;
}
