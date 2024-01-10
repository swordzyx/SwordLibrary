package sword.utils

import android.content.res.Resources
import android.graphics.Bitmap
import sword.logger.SwordLog

fun Bitmap.printDebugInfo() {
  SwordLog.debug("Bitmap 调试信息：$byteCount, allocateByte: $allocationByteCount，宽：$width, 高：$height，屏幕密度：${Resources.getSystem().displayMetrics.densityDpi}\"")
}