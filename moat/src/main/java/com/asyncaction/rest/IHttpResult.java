package com.asyncaction.rest;

import java.io.InputStream;

/**
 * Created by surecn on 15/8/20.
 */
public interface IHttpResult {

    public int getResponseCode();

    public InputStream getResponseStream();

    public String getResponseText();

    public long getContentLength();

    public String getHeader(String key);

}
