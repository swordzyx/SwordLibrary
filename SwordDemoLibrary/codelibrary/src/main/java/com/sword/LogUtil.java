package com.sword;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public class LogUtil {
  private static final String TAG = "Sword";
  private static final String LOG_SWITCH_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LoggerSwitch";
  private static final File LOG_SWITCH_FILE = new File(LOG_SWITCH_DIR
      + File.separator
      + Encryption.md5(LOG_SWITCH_DIR + File.separator + "logger_switch"));


  private static boolean isDebug() {
    return LOG_SWITCH_FILE.exists();
  }

  public static void debug(String msg) {
    debug("", msg);
  }

  public static void debug(String tag, String msg) {
    if (TextUtils.isEmpty(tag)) {
      Log.d(TAG, msg);
    } else {
      Log.d(TAG + "_" + tag, msg);
    }
  }
    
  public static void error(String msg) {
    error("", msg);
  }
  
  public static void error(String tag, String msg) {
    if (TextUtils.isEmpty(tag)) {
      Log.e(TAG, msg);
    } else {
      Log.e(TAG + "_" + tag, msg);
    }
  }

  public static void warn(String msg) {
    warn("", msg);
  }
  
  public static void warn(String tag, String msg) {
    if (TextUtils.isEmpty(tag)) {
      Log.w(TAG, msg);
    } else {
      Log.w(TAG + "_" + tag, msg);
    }
  }
}
