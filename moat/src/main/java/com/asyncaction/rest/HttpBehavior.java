package com.asyncaction.rest;

import android.content.Context;

import com.asyncaction.exception.NetworkInvalidException;
import com.asyncaction.http.HttpForm;
import com.asyncaction.rest.interceptor.RequestInterceptor;
import com.asyncaction.rest.interceptor.ResponseInterceptor;
import com.asyncaction.utils.NetworkUtils;

/**
 * Created by surecn on 15/8/20.
 */
/*package*/class HttpBehavior {

    /*package*/static Object behavior(RestAdapter adapter, MethodInfo methodInfo) throws Exception {
        final Context context = adapter.getContext();
        final RequestInterceptor requestInterceptor = adapter.getRequestInterceptor();
        final ResponseInterceptor responseInterceptor = adapter.getResponseInterceptor();
        //将方法转化为httpform
        HttpForm form = MethodBehavior.behavior(context, methodInfo.mMethod, methodInfo.mUrl, methodInfo.mArgs);
        //调用请求钱的拦截器
        if (requestInterceptor != null && !requestInterceptor.intercept(form)) {
            return null;
        }
        //请求前判断网络是否联通
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw new NetworkInvalidException();
        }
        //做http请求
        IHttpResult result = adapter.getHttpHandler().request(form);
        //处理请求结果
        Object data = responseInterceptor.intercept(result, methodInfo.mReturnType);
        return data;
    }
}
