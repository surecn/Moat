package com.asyncaction.demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.asyncaction.demo.rest.ServiceAdapter;
import com.asyncaction.action.ActionObserver;
import com.asyncaction.action.Action;
import com.asyncaction.action.ActionFlow;
import com.asyncaction.action.ActionManager;
import com.asyncaction.utils.log;

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
        ServiceAdapter.getApiService(this).getData("aaa", "bbb", "demo").setObserver(new ActionObserver() {
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
        ServiceAdapter.getApiService(this).getData("aaa", "bbb").setObserver(new ActionObserver() {
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
        ActionManager.create(new Action() {
            @Override
            public void run(Context context, ActionFlow work) throws Exception {
                String s = ServiceAdapter.getApiService(MainActivity.this).putData("aaa", "bbb", "/sdcard/launcher.db");
                work.onNext(s);
            }
        }).setObserver(new ActionObserver() {
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
        ActionManager.create(new Action() {
            @Override
            public void run(Context context, ActionFlow work) throws Exception {
                String s = ServiceAdapter.getApiService(MainActivity.this).putData("aaa", "bbb", "file:///android_asset/org.json.zip");
                work.onNext(s);
            }
        }).setObserver(new ActionObserver() {
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
//        ServiceAdapter.getApiService(this).getData("aaa", "bbb", "demo").subscribe(new ActionObserver() {
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
            ActionManager.create(new Action() {
                @Override
                public void run(Context context, ActionFlow work) throws Exception {
                    log.e(work.getPriority() + "=====start===MIN_PRIORITY===" + j);
                    Thread.sleep(5000);
                    log.e(work.getPriority() + "=====end===MIN_PRIORITY=====" + j);

                }
            }, Thread.MIN_PRIORITY).start();
        }

        for (int i = 0; i < 10; i++) {
            final int j = i;
            ActionManager.create(new Action() {
                @Override
                public void run(Context context, ActionFlow work) throws Exception {
                    log.e(work.getPriority() + "=====start===NORM_PRIORITY===" + j);
                    Thread.sleep(5000);
                    log.e(work.getPriority() + "=====end===NORM_PRIORITY=====" + j);

                }
            }, Thread.NORM_PRIORITY).start();
        }

        for (int i = 0; i < 10; i++) {
            final int j = i;
            ActionManager.create(new Action() {
                @Override
                public void run(Context context, ActionFlow work) throws Exception {
                    log.e(work.getPriority() + "=====start===MAX_PRIORITY===" + j);
                    Thread.sleep(5000);
                    log.e(work.getPriority() + "=====end===MAX_PRIORITY=====" + j);

                }
            }, Thread.MAX_PRIORITY).start();
        }
    }
}
