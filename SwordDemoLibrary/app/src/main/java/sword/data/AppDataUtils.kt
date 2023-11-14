package com.sword

import android.content.Context
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import sword.data.isSdCardExist
import java.io.File
import java.util.Random
import java.util.UUID

private const val tag = "DataStorage"

fun printAppDataDirPath(context: Context) {
  var dirInfo = "filesDir: ${context.filesDir.absolutePath} \n cacheDir: ${context.cacheDir}"
  if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
    dirInfo += "\n dataDir: ${context.dataDir}"
  }
  dirInfo += "\n codeCacheDir: ${context.codeCacheDir} \n noBackupFileDir: ${context.noBackupFilesDir} \n externalMediaDirs: ${context.externalMediaDirs}"
  dirInfo += "\n externalCahceDir: ${context.externalCacheDir}"
}


/**
 * 生成uuid
 */
fun buildUUID(name: String?): String {
  var uuid: String = if (TextUtils.isEmpty(name)) {
    UUID.randomUUID().toString().replace("-".toRegex(), "")
  } else {
    UUID.fromString(name).toString()
  }
  uuid += System.currentTimeMillis()
  return uuid
}

var s = "qwertyuiopasdfghjklzxcvbnmQWERRTYUIOPASDFGHJKLZXCVBNM1234567890"
fun randomString(count: Int): String {
  val random = Random()
  val result = StringBuilder()
  for (i in 0 until count) {
    result.append(s[random.nextInt(s.length)])
  }
  return result.toString()
}


fun getLogSwitchFile() {
  val logSwitch =
    Environment.getExternalStorageDirectory().absolutePath + File.separator + "Android" + File.separator
  //String fileNameMd5 = Encryption.md5(logSwitch + "logSwitch");
}


fun getDataDir(activity: Context): String {
  val appinfo = getAppInfo(activity)
  var path = if (isSdCardExist()) {
    //获取外部存储的根目录
    Environment.getExternalStorageDirectory().path
  } else {
    //获取内部存储中的应用私有目录
    activity.filesDir.path
  }
  path += "/" + appinfo + "_001"
  Log.d("Sword", "存储目录：$path")
  return path
}


/**
 * 获取应用包名
 */
fun getAppInfo(activity: Context): String? {
  try {
    val pkName = activity.packageName
    val versionName = activity.packageManager.getPackageInfo(
      pkName, 0
    ).versionName
    val versionCode = activity.packageManager.getPackageInfo(
      pkName, 0
    ).versionCode
    return pkName + "_" + versionName + "_" + versionCode
  } catch (ignored: Exception) {
  }
  return null
}
