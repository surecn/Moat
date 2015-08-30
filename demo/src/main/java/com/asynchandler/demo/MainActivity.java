package com.asynchandler.demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.asynchandler.demo.rest.ServiceAdapter;
import com.asynchandler.task.Task;
import com.asynchandler.task.TaskFlow;
import com.asynchandler.task.FlowManager;
import com.asynchandler.task.TaskObserver;
import com.asynchandler.utils.log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void button1Click(View view) {
        ServiceAdapter.getApiService(this).getData("aaa", "bbb", "demo").subscribe(new TaskObserver() {
            @Override
            public void onComplete(Object result) {
                super.onComplete(result);
                log.e("=================result:" + result);
                TextView tv = (TextView) findViewById(R.id.text);
                tv.setText(result.toString());
            }
        });
    }

    public void button2Click(View view) {
        ServiceAdapter.getApiService(this).getData("aaa", "bbb").subscribe(new TaskObserver() {
            @Override
            public void onComplete(Object result) {
                super.onComplete(result);
                log.e("=================result:" + result);
                TextView tv = (TextView) findViewById(R.id.text);
                tv.setText(result.toString());
            }
        });
    }

    public void button3Click(View view) {
        FlowManager.create(new Task() {
            @Override
            public void run(Context context, TaskFlow work) throws Exception {
                String s = ServiceAdapter.getApiService(MainActivity.this).putData("aaa", "bbb", "/sdcard/launcher.db");
                work.onNext(s);
            }
        }).subscribe(new TaskObserver() {
            @Override
            public void onComplete(Object result) {
                super.onComplete(result);
                log.e("=================result:" + result);
                TextView tv = (TextView) findViewById(R.id.text);
                tv.setText(result.toString());
            }
        });
    }

    public void button4Click(View view) {
        FlowManager.create(new Task() {
            @Override
            public void run(Context context, TaskFlow work) throws Exception {
                String s = ServiceAdapter.getApiService(MainActivity.this).putData("aaa", "bbb", "file:///android_asset/org.json.zip");
                work.onNext(s);
            }
        }).subscribe(new TaskObserver() {
            @Override
            public void onComplete(Object result) {
                super.onComplete(result);
                log.e("=================result:" + result);
                TextView tv = (TextView) findViewById(R.id.text);
                tv.setText(result.toString());
            }
        });
    }

    public void button5Click(View view) {
//        ServiceAdapter.getApiService(this).getData("aaa", "bbb", "demo").subscribe(new TaskObserver() {
//            @Override
//            public void onComplete(Object result) {
//                super.onComplete(result);
//                log.e("=================result:" + result);
//                TextView tv = (TextView) findViewById(R.id.text);
//                tv.setText(result.toString());
//            }
//        });



        for (int i = 0; i < 10; i++) {
            final int j = i;
            FlowManager.create(new Task() {
                @Override
                public void run(Context context, TaskFlow work) throws Exception {
                    log.e(work.getPriority() + "=====start===MIN_PRIORITY===" + j);
                    Thread.sleep(5000);
                    log.e(work.getPriority() + "=====end===MIN_PRIORITY=====" + j);

                }
            }, Thread.MIN_PRIORITY).start();
        }

        for (int i = 0; i < 10; i++) {
            final int j = i;
            FlowManager.create(new Task() {
                @Override
                public void run(Context context, TaskFlow work) throws Exception {
                    log.e(work.getPriority() + "=====start===NORM_PRIORITY===" + j);
                    Thread.sleep(5000);
                    log.e(work.getPriority() + "=====end===NORM_PRIORITY=====" + j);

                }
            }, Thread.NORM_PRIORITY).start();
        }

        for (int i = 0; i < 10; i++) {
            final int j = i;
            FlowManager.create(new Task() {
                @Override
                public void run(Context context, TaskFlow work) throws Exception {
                    log.e(work.getPriority() + "=====start===MAX_PRIORITY===" + j);
                    Thread.sleep(5000);
                    log.e(work.getPriority() + "=====end===MAX_PRIORITY=====" + j);

                }
            }, Thread.MAX_PRIORITY).start();
        }
    }
}
