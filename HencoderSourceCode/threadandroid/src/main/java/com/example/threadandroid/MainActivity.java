package com.example.threadandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    String name = "rengwuxian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HandlerThread handlerThread = new HandlerThread("handlerThread");
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    static class User {
        WeakReference<MainActivity> activityWeakReference;
        String username = activityWeakReference.get().name;
        int age;
    }

    class MyView extends View {
        String activityName = name;
        public MyView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }
    }

    class MyAsyncTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            System.out.println(name);
            super.onPreExecute();
        }

        //此方法在后台自动执行
        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        //此方法在启动 AsyncTask 的线程中执行，线程切换是由 AsyncTask 自动完成的。
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}