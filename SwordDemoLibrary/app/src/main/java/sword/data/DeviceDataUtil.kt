package sword.data

import android.os.Environment

/**
 * 判断设备外部存储当前是否可以访问。（如果外部存储目录被挂在到了计算机上，应用程序将无法访问）
 */
fun isSdCardExist(): Boolean {
  try {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return false
}