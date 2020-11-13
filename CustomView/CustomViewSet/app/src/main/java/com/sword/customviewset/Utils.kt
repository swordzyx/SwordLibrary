package com.sword.customviewset

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun getAvator(res: Resources, width: Int, resId: Int = R.drawable.avatar_rengwuxian): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId)
    options.inDensity = options.outWidth
    options.inJustDecodeBounds = false
    options.inTargetDensity = width
    return BitmapFactory.decodeResource(res, resId)
}