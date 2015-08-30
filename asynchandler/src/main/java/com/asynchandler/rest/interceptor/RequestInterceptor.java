package com.asynchandler.rest.interceptor;


import com.asynchandler.http.HttpForm;

/**
 * Created by surecn on 15/8/5.
 * 请求前拦截器
 */
public interface RequestInterceptor {
    /**
     * 拦截请求对httpform做处理
     * @param form
     * @return
     * @throws Exception
     */
    public boolean intercept(HttpForm form) throws Exception;
}
