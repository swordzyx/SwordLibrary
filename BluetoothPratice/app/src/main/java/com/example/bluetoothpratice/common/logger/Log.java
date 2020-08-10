package com.example.bluetoothpratice.common.logger;

public class Log {
    public static final int NONE = -1;
    public static final int VERBOSE = android.util.Log.VERBOSE;
    public static final int INFO = android.util.Log.INFO;
    public static final int DEBUG = android.util.Log.DEBUG;
    public static final int WARN = android.util.Log.WARN;
    public static final int ERROR = android.util.Log.ERROR;
    public static final int ASSERT = android.util.Log.ASSERT;

    private static LogNode logNode;

    public static void setLogNode(LogNode node){
        logNode = node;
    }

    private static void println(int priority, String tag, String text, Throwable tr){
        if (logNode != null){
            logNode.println(priority, tag, text, tr);
        }
    }

    public static void v(String tag, String text){
        println(VERBOSE, tag, text, null);
    }

    public static void i(String tag, String text){
        println(INFO, tag, text, null);
    }

    public static void d(String tag, String text){
        println(DEBUG, tag, text, null);
    }

    public static void w(String tag, String text){
        println(WARN, tag, text, null);
    }

    public static void e(String tag, String text){
        println(ERROR, tag, text, null);
    }

    public static void a(String tag, String text){
        println(ASSERT, tag, text, null);
    }

}
