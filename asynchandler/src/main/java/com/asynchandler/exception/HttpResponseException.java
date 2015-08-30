package com.asynchandler.exception;

import android.content.Context;
import android.widget.Toast;

public class HttpResponseException extends BaseException {

	private int mCode;

	public HttpResponseException(int code) {
		mCode = code;
	}

	@Override
	public boolean handle(Context context) {
		Toast.makeText(context, "服务器处理错误--code:" + mCode + "]", Toast.LENGTH_LONG).show();
		return false;
	}

}
