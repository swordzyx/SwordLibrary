package com.example.utilclass;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class LogUtil {
    private static final String TAG = "Sword"; 
    private static final String LOG_SWITCH_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LoggerSwitch";
    
    public LogUtil() {
        String switchFileName = Encryption.md5(LOG_SWITCH_DIR + File.separator + "logger_switch");
        
    }
    
    private static boolean isDebug() {
        
    }
    
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
