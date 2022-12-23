package com.sword

import android.content.Context
import android.os.Build

private const val TAG = "DataStorage"

fun printAppDataDirPath(context: Context) {
  var dirInfo = "filesDir: ${context.filesDir.absolutePath} \n cacheDir: ${context.cacheDir}"
  if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
    dirInfo += "\n dataDir: ${context.dataDir}"
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    dirInfo += "\n codeCacheDir: ${context.codeCacheDir} \n noBackupFileDir: ${context.noBackupFilesDir} \n externalMediaDirs: ${context.externalMediaDirs}"
  } 
  dirInfo += "\n externalCahceDir: ${context.externalCacheDir}"
}