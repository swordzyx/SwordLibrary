package sword.utils

import android.app.Activity

object AndroidUtilities {
  /**
   * 获取应用包名
   */
  fun Activity.appInfo(): String? {
    try {
      val versionName = packageManager.getPackageInfo(packageName, 0).versionName
      val versionCode = packageManager.getPackageInfo(packageName, 0).versionCode
      return packageName + "_" + versionName + "_" + versionCode
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return null
  }
}