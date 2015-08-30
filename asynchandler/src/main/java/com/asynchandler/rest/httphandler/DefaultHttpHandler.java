package com.asynchandler.rest.httphandler;

import com.asynchandler.http.HttpForm;
import com.asynchandler.http.HttpRequest;
import com.asynchandler.rest.IHttpResult;
import com.asynchandler.rest.httphandler.IHttpHandler;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-30
 * Time: 15:17
 */
public class DefaultHttpHandler implements IHttpHandler {
    @Override
    public IHttpResult request(HttpForm form) throws Exception {
        return HttpRequest.request(form);
    }
}
