package com.asynchandler.exception;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by surecn on 15/8/9.
 */
public class NetworkInvalidException extends BaseException {
    @Override
    public boolean handle(Context context) {
        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
        return false;
    }
}
