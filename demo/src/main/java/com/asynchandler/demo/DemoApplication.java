package com.asynchandler.demo;

import android.app.Application;
import android.widget.Toast;

import com.asynchandler.exception.BaseException;
import com.asynchandler.task.ErrorInterceptor;
import com.asynchandler.task.FlowManager;
import org.apache.http.conn.HttpHostConnectException;
import java.net.SocketException;

/**
 * User: surecn(surecn@163.com)
 * Date: 2015-08-21
 * Time: 10:16
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化任务队列,设置出现异常后的默认处理,可以自定义添加
        initWorkFlow();
    }

    public void initWorkFlow() {
        FlowManager workFlow = FlowManager.getInstance();
        workFlow.init(this);
        workFlow.setErrorInterceptor(new ErrorInterceptor() {
            @Override
            public boolean interceptor(Throwable throwable) {
                if (throwable instanceof HttpHostConnectException) {
                    HttpHostConnectException hhce = (HttpHostConnectException) throwable;
                    Toast.makeText(DemoApplication.this, "服务器连接出错:" + hhce.getHost().getHostName(), Toast.LENGTH_LONG).show();
                    return true;
                } else if (throwable instanceof SocketException) {
                    Toast.makeText(DemoApplication.this, "网络数据传输出错", Toast.LENGTH_LONG).show();
                    return true;
                } else if (throwable instanceof BaseException) {
                    ((BaseException) throwable).handle(DemoApplication.this);
                }
                return false;
            }
        });
    }
}
