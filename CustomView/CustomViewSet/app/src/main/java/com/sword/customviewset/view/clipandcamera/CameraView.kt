package com.sword.customviewset.view.clipandcamera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sword.customviewset.R
import com.sword.customviewset.view.dp

class CameraView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var topRotation = 0f
        set(value) {
            field = value
            invalidate()
        }
    var bottomRotation = 30f
        set(value) {
            field = value
            invalidate()
        }
    var divideRotation = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val BITMAP_SIZE = 200.dp
    private val BITMAP_PADDING = 100.dp
    private var camera = Camera().apply {
        //围绕 x 为中心轴旋转 30 度，原点为轴心
        setLocation(0f, 0f, -6*resources.displayMetrics.density)
    }
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)


    override fun onDraw(canvas: Canvas) {
        //上半部分
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2 , BITMAP_PADDING + BITMAP_SIZE/2)
        canvas.rotate(-divideRotation)
        camera.save()
        camera.rotateX(topRotation)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(-BITMAP_SIZE, -BITMAP_SIZE, BITMAP_SIZE, 0f)
        canvas.rotate(divideRotation)
        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE/2), -(BITMAP_PADDING + BITMAP_SIZE/2))
        canvas.drawBitmap(getAvator(BITMAP_SIZE.toInt()), BITMAP_PADDING, BITMAP_PADDING, paint)
        canvas.restore()

        //下半部分
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)
        canvas.rotate(-divideRotation)
        camera.save()
        camera.rotateX(bottomRotation)
        camera.applyToCanvas(canvas)
        canvas.restore()
        canvas.clipRect(-BITMAP_SIZE, 0f, BITMAP_SIZE, BITMAP_SIZE)
        canvas.rotate(divideRotation)
        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE/2 ), -(BITMAP_PADDING + BITMAP_SIZE/2))
        canvas.drawBitmap(getAvator(BITMAP_SIZE.toInt()), BITMAP_PADDING, BITMAP_PADDING, paint)
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