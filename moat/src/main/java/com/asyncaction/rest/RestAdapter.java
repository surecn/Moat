package com.asyncaction.rest;

import android.content.Context;
import android.webkit.URLUtil;

import com.asyncaction.rest.httphandler.DefaultHttpHandler;
import com.asyncaction.rest.httphandler.IHttpHandler;
import com.asyncaction.rest.interceptor.RequestInterceptor;
import com.asyncaction.rest.interceptor.ResponseInterceptor;
import com.asyncaction.rest.interceptor.StringResponseInterceptor;

/**
 * Created by surecn on 15/8/3.
 */
public class RestAdapter {

    private Context mContext;

    private RequestInterceptor mRequestInterceptor;

    private ResponseInterceptor mResponseInterceptor;

    private IHttpHandler mHttpHandler;

    private String mURL;

    private static RestAdapter sRestAdapter;

    /**
     * 获取适配器
     * @param context
     * @param url 设置url
     * @return
     */
    public static RestAdapter getAdapter(Context context, String url) {
        if (URLUtil.isHttpUrl(url) ||
                URLUtil.isHttpsUrl(url)) {
            if (sRestAdapter == null) {
                sRestAdapter = new RestAdapter(context, url);
            }
            return sRestAdapter;
        }
        return null;
    }

    private RestAdapter(Context context, String url) {
        mContext = context;
        mURL = url;
    }

    /**
     * 创建Http接口的Service
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> cls) {
        if (cls.isInterface()) {
            RestProxy proxy = new RestProxy(this, mURL);
            return proxy.create(cls);
        }
        return null;
    }

    /**
     * 设置http请求之前的拦截器
     * @param requestInterceptor
     */
    public void setRequestInterceptor(RequestInterceptor requestInterceptor) {
        if (requestInterceptor != null) {
            mRequestInterceptor = requestInterceptor;
        }
    }

    /*package*/RequestInterceptor getRequestInterceptor() {
        return mRequestInterceptor;
    }

    /**
     * 设置Response的拦截器,缺省下StringResponseInterceptor
     * @param responseInterceptor 可使用,StringResponseInterceptor,JSONResponseInterceptor,也可以实现ResponseInterceptor接口
     */
    public void setResponseInterceptor(ResponseInterceptor responseInterceptor) {
        if (responseInterceptor != null) {
            mResponseInterceptor = responseInterceptor;
        }
    }

    /*package*/ResponseInterceptor getResponseInterceptor() {
        if (mResponseInterceptor == null) {
            mResponseInterceptor = new StringResponseInterceptor();
        }
        return mResponseInterceptor;
    }

    /**
     * 设置Http请求方法, 缺省下为AsyncHandler自带的http请求
     * @param httpHandler
     */
    public void setHttpHandler(IHttpHandler httpHandler) {
        if (httpHandler != null) {
            mHttpHandler = httpHandler;
        }
    }

    /*package*/IHttpHandler getHttpHandler() {
        if (mHttpHandler == null) {
            mHttpHandler = new DefaultHttpHandler();
        }
        return mHttpHandler;
    }

    /*package*/Context getContext() {
        return mContext;
    }
}
