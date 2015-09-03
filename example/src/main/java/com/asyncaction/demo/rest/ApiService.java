package com.asyncaction.demo.rest;

import com.asyncaction.rest.annotation.FIELD;
import com.asyncaction.rest.annotation.FILE;
import com.asyncaction.rest.annotation.GET;
import com.asyncaction.rest.annotation.HEADER;
import com.asyncaction.rest.annotation.PATH;
import com.asyncaction.rest.annotation.POST;
import com.asyncaction.rest.annotation.PUT;
import com.asyncaction.rest.annotation.TIMEOUT;
import com.asyncaction.action.ActionFlow;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-21
 * Time: 10:16
 */
public interface ApiService {

    /*超时时间，默认30秒*/
    @TIMEOUT(20000)
    @GET("/demo")
    public ActionFlow<String> getData(@FIELD("name") String name, @FIELD("datetime") String time);

    @POST("/{path}")
    public ActionFlow<String> getData(@FIELD("name") String name, @FIELD("datetime") String time, @PATH("path") String path);

    @PUT("/demo")
    public String putData(@FIELD("name") String name, @FIELD("datetime") String time, @FILE("path") String path);

    @GET("/demo")
    public String loadData(@FIELD("name") String name, @FIELD("datetime") String time, @HEADER("cookie") String path);
}
