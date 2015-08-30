package com.asynchandler.exception;

import android.content.Context;


public abstract class BaseException extends Exception {
	
	public BaseException() {
	}
	
	public BaseException(Exception e) {
		super(e.getMessage(), e.getCause());
	}
	
	public BaseException(String message) {
		super(message);
	}
	
	public abstract boolean handle(Context context);

}
