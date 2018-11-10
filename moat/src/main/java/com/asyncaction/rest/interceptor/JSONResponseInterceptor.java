package com.asyncaction.rest.interceptor;

import com.asyncaction.exception.HttpResponseException;
import com.asyncaction.rest.IHttpResult;
import com.asyncaction.utils.IOUtils;
import com.asyncaction.utils.log;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by surecn on 15/8/20.
 * 处理{code: 200, msg: "", data:{}}这种返回格式的数据
 * service方法的返回结果返回data的值
 */
public class JSONResponseInterceptor implements ResponseInterceptor {

    @Override
    public Object intercept(IHttpResult result, Type t) throws Exception {
        String text = IOUtils.readString(result.getResponseStream());
        log.e("HTTP ResponseText:" + text);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Record.class, objectMapper.getTypeFactory().constructType(t));
        Record res = null;
        try {
            res = objectMapper.readValue(text, javaType);
            if (res != null) {
                //正确时将data返回
                if (res.code == 200) {
                    return res.data;
                }
                //错误的code统一放到HttpResponseException中做处理
                throw new HttpResponseException(res.code);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Record<T> {
        public int code;
        public T data;
    }
}
