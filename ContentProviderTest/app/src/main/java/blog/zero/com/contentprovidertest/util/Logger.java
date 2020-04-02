package blog.zero.com.contentprovidertest.util;

import android.util.Log;

public class Logger {
    private static final Boolean DEBUG = true;
    private static final String DEFAULTTAG = "TestContentProvider";

    public static void debug(String info){
        if (DEBUG)
            debug(DEFAULTTAG, info);
    }

    public static void info(String info){
        if (DEBUG)
            Log.i(DEFAULTTAG, info);
    }

    public static void error(String info){
        if (DEBUG)
            Log.e(DEFAULTTAG, info);
    }

    public static void debug(String tag, String info){
        if (DEBUG)
            Log.d(tag, info);
    }

    public static void info(String tag, String info){
        if (DEBUG)
            Log.i(tag, info);
    }

    public static void error(String tag, String info){
        if (DEBUG)
            Log.e(tag, info);
    }
}
