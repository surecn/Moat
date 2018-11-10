package com.asyncaction.rest.httphandler;

import com.asyncaction.http.HttpForm;
import com.asyncaction.http.HttpRequest;
import com.asyncaction.rest.IHttpResult;

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
