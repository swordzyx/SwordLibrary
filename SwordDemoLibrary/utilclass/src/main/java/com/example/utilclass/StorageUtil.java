package com.example.utilclass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

public class StorageUtil {
  private BroadcastReceiver externalStorageReceiver;

  private boolean externalStroageAvailable = false;
  private boolean externalStorageWritable = false;

  /**
   * 注册广播接收器实现监听外部存储目录状态的更改，当外部存储目录挂载或者被移除时触发 {@link StorageUtil#updateExternalStorageState()} 回调。
   */
  public void startWatchingExternalStorage(Context context) {
    externalStorageReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        LogUtil.debug("Storage: " + intent.getData());
        updateExternalStorageState();
      }
    };

    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
    filter.addAction(Intent.ACTION_MEDIA_REMOVED);
    context.registerReceiver(externalStorageReceiver, filter);
    updateExternalStorageState();
  }


  private void updateExternalStorageState() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      externalStroageAvailable = externalStorageWritable = true;
    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      externalStroageAvailable = true;
      externalStorageWritable = false;
    } else {
      externalStroageAvailable = externalStorageWritable = false;
    }

    //todo: handleExternalStorageState(externalStorageAvailable, externalStorageWritable)
  }


}
