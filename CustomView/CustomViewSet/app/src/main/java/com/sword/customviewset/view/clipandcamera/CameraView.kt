package com.sword.customviewset.view.clipandcamera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sword.customviewset.R
import com.sword.customviewset.view.dp

class CameraView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val BITMAP_SIZE = 200.dp
    private val BITMAP_PADDING = 100.dp
    private var camera = Camera().apply {
        //围绕 x 为中心轴旋转 30 度，原点为轴心
        rotateX(30f)
    }
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)


    override fun onDraw(canvas: Canvas) {
        //上半部分
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2 , BITMAP_PADDING + BITMAP_SIZE/2)
        canvas.clipRect(-BITMAP_SIZE/2, -BITMAP_SIZE/2, BITMAP_SIZE/2, 0f)
        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE/2), -(BITMAP_PADDING + BITMAP_SIZE/2))
        canvas.drawBitmap(getAvator(BITMAP_SIZE.toInt()), BITMAP_PADDING, BITMAP_PADDING, paint)
        canvas.restore()

        //下半部分
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)
        camera.applyToCanvas(canvas)
        canvas.clipRect(-BITMAP_SIZE/2, 0f, BITMAP_SIZE/2, BITMAP_SIZE/2)
        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE/2 ), -(BITMAP_PADDING + BITMAP_SIZE/2))
        canvas.restore()
    }

    fun getAvator(width: Int, resId: Int = R.drawable.avatar_rengwuxian): Bitmap {
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, resId, options)
    }
}