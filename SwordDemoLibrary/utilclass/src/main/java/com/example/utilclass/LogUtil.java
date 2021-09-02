package com.example.utilclass;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "Sword"; 
    public static void debug(String msg) {
        Log.d(TAG, msg);
    }
    
    public static void error(String msg) {
        Log.d(TAG, msg);
    }
    
    public static void warn(String msg) {
        Log.d(TAG, msg);
    }
}
