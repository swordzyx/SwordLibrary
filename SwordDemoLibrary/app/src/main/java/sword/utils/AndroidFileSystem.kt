package sword.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import sword.logger.SwordLog

object AndroidFileSystem {
  private const val tag = "AndroidFileSystem"

  /**
   * 打印 Android 文件系统相关信息
   */
  fun printFileSystemInfo(activity: Activity) {
    SwordLog.debug(tag, "FilesDir: ${activity.filesDir.absolutePath}")
    SwordLog.debug(tag, "cacheDir: ${activity.cacheDir.absolutePath}")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      SwordLog.debug(tag, "dataDir: ${activity.dataDir.absolutePath}")
    }
    SwordLog.debug(tag, "codeCacheDir: ${activity.codeCacheDir.absolutePath}")
    activity.externalCacheDirs?.forEach {
      SwordLog.debug(tag, "externalCacheDir: ${it.absolutePath}")
    }
    activity.externalMediaDirs?.forEach {
      SwordLog.debug(tag, "externalMediaDir: ${it.absolutePath}")
    }
  }

  /**
   * 注册广播接收器实现监听外部存储目录状态的更改，当外部存储目录挂载或者被移除时触发 [updateExternalStorageState] 回调。
   */
  fun startWatchingExternalStorage(context: Context) {
    val externalStorageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        SwordLog.debug("Storage: " + intent.data)
        updateExternalStorageState()
      }
    }
    val filter = IntentFilter()
    filter.addAction(Intent.ACTION_MEDIA_MOUNTED)
    filter.addAction(Intent.ACTION_MEDIA_REMOVED)
    context.registerReceiver(externalStorageReceiver, filter)
    updateExternalStorageState()
  }


  /**
   * 更新外部存储目录状态，执行对应的 app 逻辑
   */
  private fun updateExternalStorageState() {
    val state = Environment.getExternalStorageState()
    if (Environment.MEDIA_MOUNTED == state) {
      SwordLog.debug("externalStorageState：Environment.MEDIA_MOUNTED，外部存储是否可用：true，外部存储是否可写: true")
    } else if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
      SwordLog.debug("externalStorageState：Environment.MEDIA_MOUNTED_READ_ONLY，外部存储是否可用：true，外部存储是否可写: false")
    } else {
      SwordLog.debug("externalStorageState：未知，外部存储是否可用：true，外部存储是否可写: false")
    }

    //todo: handleExternalStorageState(externalStorageAvailable, externalStorageWritable)
  }
  
  
  /**
   * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
   */
  val sdCardExist: Boolean
    get() {
      try {
        return Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED
      } catch (e: Exception) {
        e.printStackTrace()
      }
      return false
    }
}