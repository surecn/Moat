package com.asynchandler.demo.rest;

import com.asynchandler.rest.annotation.FIELD;
import com.asynchandler.rest.annotation.FILE;
import com.asynchandler.rest.annotation.GET;
import com.asynchandler.rest.annotation.HEADER;
import com.asynchandler.rest.annotation.PATH;
import com.asynchandler.rest.annotation.POST;
import com.asynchandler.rest.annotation.PUT;
import com.asynchandler.rest.annotation.TIMEOUT;
import com.asynchandler.task.TaskFlow;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-21
 * Time: 10:16
 */
public interface ApiService {

    /*超时时间，默认30秒*/
    @TIMEOUT(20000)
    @GET("/demo")
    public TaskFlow<String> getData(@FIELD("name") String name, @FIELD("datetime") String time);

    @POST("/{path}")
    public TaskFlow<String> getData(@FIELD("name") String name, @FIELD("datetime") String time, @PATH("path") String path);

    @PUT("/demo")
    public String putData(@FIELD("name") String name, @FIELD("datetime") String time, @FILE("path") String path);

    @GET("/demo")
    public String loadData(@FIELD("name") String name, @FIELD("datetime") String time, @HEADER("cookie") String path);
}
