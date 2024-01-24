package sword.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.text.TextUtils
import sword.logger.SwordLog

fun Bitmap.printDebugInfo() {
  SwordLog.debug("Bitmap 调试信息：$byteCount, allocateByte: $allocationByteCount，宽：$width, 高：$height，屏幕密度：${Resources.getSystem().displayMetrics.densityDpi}\"")
}

fun Bitmap.printDebugInfo(tag: String) {
  if (!TextUtils.isEmpty(tag))
    SwordLog.debug(
      tag,
      "Bitmap 调试信息：$byteCount, allocateByte: $allocationByteCount，宽：$width, 高：$height，屏幕密度：${Resources.getSystem().displayMetrics.densityDpi}\""
    )
}