package com.asynchandler.demo.rest;

import android.content.Context;

import com.asynchandler.exception.HttpResponseException;
import com.asynchandler.rest.RestAdapter;
import com.asynchandler.rest.interceptor.JSONResponseInterceptor;
import com.asynchandler.utils.IOUtils;
import com.asynchandler.utils.log;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-21
 * Time: 10:16
 */
public class ServiceAdapter {

    public final static String URL = "http://121.40.157.245/ElegantWeb";

    public static ApiService getApiService(Context context) {
        RestAdapter adapter = RestAdapter.getAdapter(context, URL);

        adapter.setResponseInterceptor(new JSONResponseInterceptor());
        //处理responseText:{code:'1', data:{}, message:""} 这种返回结果的demo
//        adapter.setResponseInterceptor(new ResponseInterceptor() {
//            @Override
//            public Object intercept(IHttpResult result, Type t) throws Exception {
//                String text = IOUtils.readString(result.getResponseStream());
//                log.e("HTTP ResponseText:" + text);
//                ObjectMapper objectMapper = new ObjectMapper();
//                objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Record.class, objectMapper.getTypeFactory().constructType(t));
//                Record res = null;
//                try {
//                    res = objectMapper.readValue(text, javaType);
//
//                    if (res != null) {
//                        //正确时将data返回
//                        if (res.code == 200) {
//                            return res.data;
//                        }
//                        //错误的code统一放到HttpResponseException中做处理
//                        throw new HttpResponseException(res.code);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        });

        return adapter.create(ApiService.class);
    }

    public static class Record<T> {
        public int code;
        public T data;
    }
}
