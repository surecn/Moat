package com.asyncaction.exception;

import android.content.Context;
import android.widget.Toast;

public class HttpRequestException extends BaseException {
	
	private int mCode;
	
	public HttpRequestException() {
	}

	@Override
	public boolean handle(Context context) {
		Toast.makeText(context, "服务器处理错误--code:" + mCode + "]", Toast.LENGTH_LONG).show();
		return false;
	}

}
