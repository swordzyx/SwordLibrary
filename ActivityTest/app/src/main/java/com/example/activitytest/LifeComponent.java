package com.example.activitytest;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.activitytest.util.Constants;

public class LifeComponent implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void createEvent(){
        Log.d(Constants.TAG, "oncreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resumeEvent(){
        Log.d(Constants.TAG, "resume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pauseEvent(){
        Log.d(Constants.TAG, "pause");
    }
}
