package sword

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import sword.logger.SwordLog
import sword.utils.printDebugInfo

object BitmapUtil {
    private const val tag = "BitmapUtil"
    /**
     * 将图片缩放至目标大小，减少显示时的内存占用
     *
     * todo：此接口待优化
     */
    fun createBitmap(resource: Resources, resId: Int, targetWidth: Float, targetHeight: Float): Bitmap {
        val bitmaptOption = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(resource, resId, this)
            val originalWidth = outWidth
            val originalHeight = outHeight
            
            val scaleWidth = targetWidth / originalWidth
            val scaleHeight = targetHeight / originalHeight
            if (scaleWidth > scaleHeight) {
                inDensity = outWidth
                inTargetDensity = targetWidth.toInt()
            } else {
                inDensity = outHeight
                inTargetDensity = targetHeight.toInt()
            }
            
            SwordLog.debug(tag,"targetWidth: $targetWidth, targetHeight: $targetHeight, oldWidth: $originalWidth, oldHeight: $originalHeight, sampleSize: $inSampleSize, scaleWidth: $scaleWidth, scaleHeight: $scaleHeight")
            inJustDecodeBounds = false
            inScaled = true
        }
        SwordLog.debug(tag,"inSampleSize: ${bitmaptOption.inSampleSize}, targetDensity: ${bitmaptOption.inTargetDensity}, inDensity: ${bitmaptOption.inDensity}")
        val resultBitmap = BitmapFactory.decodeResource(resource, resId, bitmaptOption) 
        resultBitmap.printDebugInfo(tag)
        return resultBitmap
    }
}

