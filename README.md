# AsyncHandler
* 邮箱:surecn@163.com
* http://surecn.iteye.com/
* 希望结识爱好开发的朋友,一起成长

#简介
这几年做android开发，在做http后台调用时，用过几种方法，有对asynctask进行再次封装，还有用handler做为回调, 但使用下来都不是特别理想，去年就了解了RxAndroid + Retrofit, 今年在一个项目中经过一些使用，感觉RxAndroid将每个处理封装成一个任务单元，并且采用订阅者的方式做ui界面更新的这种设计方式非常好，但是在写的过程中，觉得Rxandroid使用还是有些复杂，比如要需要设置使用线程什么执行，设置订阅者太过灵活，并且不能做一些统一的异常处理， 所以参考Rxandroid的设计方法，并且结合AsyncTask的一些优点，重新设计了一个类似的框架，虽然是重复造轮子，但却是是好用很多，由于Retrofit不能和AsyncHandler结合，所以参考Retrofit, 也设计了一套类似的rest接口调用框架，让http的调用更加简单

* 权限
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
<uses-permission android:name="android.permission.INTERNET"></uses-permission>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
```

#TaskFlow的使用方法
1.初始化
```java
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
```
2.调用
```java
FlowManager.create(new Task() {
    @Override
    public void run(Context context, TaskFlow work) throws Exception {
        Log.i("", "execute");
        work.onNext(s);
    }
}).subscribe(new TaskObserver() {
    @Override
    public void onComplete(Object result) {
        super.onComplete(result);
    }
});
```
#Rest

1.Service的写法
```java
public interface ApiService {
    /*超时时间，默认30秒*/
    @TIMEOUT(20000)
    @GET("/demo")
    public TaskFlow<Pojo> getData(@FIELD("name") String name, @FIELD("datetime") String time);

    @POST("/{path}")
    public TaskFlow<String> getData(@FIELD("name") String name, @FIELD("datetime") String time, @PATH("path") String path);

    @PUT("/demo")
    public String putData(@FIELD("name") String name, @FIELD("datetime") String time, @FILE("path") String path);

    @GET("/demo")
    public String loadData(@FIELD("name") String name, @FIELD("datetime") String time, @HEADER("cookie") String path);
}
```

2.获取adapter
```java
public static ApiService getApiService(Context context) {
  RestAdapter adapter = RestAdapter.getAdapter(context, URL);
  //处理responseText:{code:'1', data:{}, message:""} 这种返回结果的demo
  adapter.setResponseInterceptor(new JSONResponseInterceptor());
  
  //自定义处理, 可以看考JSONResponseInterceptor
  //adapter.setResponseInterceptor(new ResponseInterceptor() {
  //    @Override
  //    public Object intercept(IHttpResult result, Type t) throws Exception {
  //        return null;
  //    }
  //});
  return adapter.create(ApiService.class);
}
```
3.TaskFlow + Rest
```java
FlowManager.create(new Task() {
  @Override
  public void run(Context context, TaskFlow work) throws Exception {
      String s = ServiceAdapter.getApiService(MainActivity.this).putData("aaa", "bbb", "/sdcard/text.log");
      work.onNext(s);
  }
}).subscribe(new TaskObserver() {
  @Override
  public void onComplete(Object result) {
      super.onComplete(result);
      TextView tv = (TextView) findViewById(R.id.text);
      tv.setText(result.toString());
  }
});
```
