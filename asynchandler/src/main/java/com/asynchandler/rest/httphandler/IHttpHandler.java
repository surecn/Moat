package com.asynchandler.rest.httphandler;

import com.asynchandler.http.HttpForm;
import com.asynchandler.rest.IHttpResult;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-30
 * Time: 14:54
 */
public interface IHttpHandler {
    public IHttpResult request(HttpForm form) throws Exception;
}
