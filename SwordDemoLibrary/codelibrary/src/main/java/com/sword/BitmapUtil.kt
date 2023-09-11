package com.sword

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.max

/**
 * 将图片缩放至目标大小，减少显示时的内存占用
 */
fun createBitmap(resource: Resources, resId: Int, targetWidth: Float, targetHeight: Float): Bitmap {
    val bitmaptOption = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(resource, resId, this)
        val oldWidth = outWidth
        val oldHeight = outHeight

        var sampleSize = 1f

        val scale = max(1f, max(oldWidth / targetWidth, oldHeight / targetHeight))
        while (sampleSize < scale) {
            sampleSize *= 2f
        }
        sword.LogUtil.debug("targetWidth: $targetWidth, targetHeight: $targetHeight, oldWidth: $oldWidth, oldHeight: $oldHeight, sampleSize: $sampleSize, scale: $scale")
        inJustDecodeBounds = false
        inSampleSize = sampleSize.toInt()
    }
    return BitmapFactory.decodeResource(resource, resId, bitmaptOption)
}


fun createBitmap1(resource: Resources, resId: Int, targetWidth: Float, targetHeight: Float): Bitmap {
    val bitmaptOption = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(resource, resId, this)

        inDensity = outWidth
        inJustDecodeBounds = false
        inTargetDensity = targetWidth.toInt()
        inScaled = true
    }
    return BitmapFactory.decodeResource(resource, resId, bitmaptOption)
}
