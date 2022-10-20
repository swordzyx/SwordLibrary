package com.example.swordlibrary.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.swordlibrary.R;

public class MemoryListenerActivity extends AppCompatActivity implements ComponentCallbacks2 {
    String TAG = "SWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_listener);

        getMemoryInfo();
    }

    private ActivityManager.MemoryInfo getMemoryInfo() {
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        StringBuilder memoryString = new StringBuilder();

        memoryString.append("\n当前可用内存：").append(memoryInfo.availMem/1024/1024).append("M\n总内存：").append(memoryInfo.totalMem/1024/1024).append("M\n内存还剩：").append(memoryInfo.threshold/1024/1024).append("M 将触发进程回收").append("\n设备是否内存不足：").append(memoryInfo.lowMemory);

        Log.d(TAG, memoryString.toString());
        return memoryInfo;
    }

    @Override
    public void onTrimMemory(int level) {
        //通过 level 确定触发了哪个生命周期或系统事件
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                //UI 已对用户隐藏，释放所有占用了内存的 UI 对象
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                //
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                //释放非应用运行所必须的内存，当前设备内存已不足，系统将开始终止后台进程。
                break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                //尽可能的释放更多的内存。应用进程处于 LRU（最近最少使用） 列表的尾部，此应用进程会首先被系统杀死。
                break;
            default:
                //释放任何非关键的数据结构。
                //应用收到无法识别的内存级别的值，一般被视为低内存消息。
                break;
        }
    }

    //onTrimMemory 是 Android 4.0 增加的接口，Android 4.0 以前，使用此回调代替 TRIM_MEMORY_COMPLETE 级别的内存事件
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}