package com.example.utilclass

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.max
import kotlin.math.min

/**
 * 将图片缩放至目标大小，减少显示时的内存占用
 */
private fun scalePic(sourcePicPath: String, targetWidth: Int, targetHeight: Int) : Bitmap {

    //获取缩小因子。min(原图宽/目标宽，原图高/目标高)
    val bmOptions = BitmapFactory.Options().apply {
        //获取图片原始宽高
        inJustDecodeBounds = true
        BitmapFactory.decodeFile(sourcePicPath, this)
        val photoW = outWidth
        val photoH = outHeight

        //缩放系数应该大于 1 ，小于 1 将会使图片放大
        val scaleFactor = max(1, min(photoW / targetWidth, photoH / targetHeight))
        inJustDecodeBounds = false
        inSampleSize = scaleFactor
        inPurgeable = true
    }

    return BitmapFactory.decodeFile(sourcePicPath, bmOptions)
}