package com.asyncaction.rest;

import android.content.Context;

import com.asyncaction.http.HttpFileBody;
import com.asyncaction.http.HttpForm;
import com.asyncaction.rest.annotation.FIELD;
import com.asyncaction.rest.annotation.FILE;
import com.asyncaction.rest.annotation.GET;
import com.asyncaction.rest.annotation.HEADER;
import com.asyncaction.rest.annotation.PATH;
import com.asyncaction.rest.annotation.POST;
import com.asyncaction.rest.annotation.PUT;
import com.asyncaction.rest.annotation.TIMEOUT;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by surecn on 15/8/5.
 */
/*package*/class MethodBehavior {

    /*package*/static HttpForm behavior(Context context, Method method, String url, Object [] args) {
        Annotation[][] annoParams = method.getParameterAnnotations();
        HttpForm form = new HttpForm("");

        //处理方法的注解
        for(Annotation anno : method.getDeclaredAnnotations()) {
            if (anno instanceof GET) {
                form.setMethod(HttpForm.HttpMethod.GET);
                form.setUrl(url + ((GET) anno).value());
            } else if (anno instanceof POST) {
                form.setMethod(HttpForm.HttpMethod.POST);
                form.setUrl(url + ((POST) anno).value());
            } else if (anno instanceof PUT) {
                form.setMethod(HttpForm.HttpMethod.PUT);
                form.setUrl(url + ((PUT) anno).value());
            } else if (anno instanceof TIMEOUT) {
                form.setTimeout(((TIMEOUT) anno).value());
            }
        }

        //处理参数的注解
        for (int i = 0, len = annoParams.length; i < len; i++) {
            String name = "";
            for (Annotation an : annoParams[i]) {
                if (an instanceof FILE) {
                    name = ((FILE) an).value();
                    form.addFileBody(new HttpFileBody(context, name, String.valueOf(args[i]), ""));
                    break;
                } else if (an instanceof FIELD) {
                    name = ((FIELD) an).value();
                    form.addTextParameter(name, String.valueOf(args[i]));
                    break;
                } else if (an instanceof HEADER) {
                    name = ((HEADER) an).value();
                    form.addTextParameter(name, String.valueOf(args[i]));
                } else if (an instanceof PATH) {
                    name = ((PATH) an).value();
                    form.setUrl(form.getUrl().replace("{" + name + "}", String.valueOf(args[i])));
                }
            }
        }
        return form;
    }

}
