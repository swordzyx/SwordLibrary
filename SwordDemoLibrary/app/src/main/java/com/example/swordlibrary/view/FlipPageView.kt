package com.example.swordlibrary.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.swordlibrary.R
import com.sword.createBitmap1
import com.sword.dp2px

/**
 * 翻页效果 View
 */
class FlipPageView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val angle = 30f
    private val imageWidth = dp2px(200f)
    private val imageBitmap =
        createBitmap1(resources, R.drawable.avatar_rengwuxian, imageWidth, imageWidth)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera().apply {
        setLocation(0f, 0f, locationZ * Resources.getSystem().displayMetrics.density)
        rotateX(angle + 10)
    }

    init {
        setPadding(100, 100, 0, 0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> (paddingLeft + imageWidth + paddingRight).toInt()
            else -> MeasureSpec.getSize(widthMeasureSpec)
        }
        val measureHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> (paddingTop + imageWidth + paddingBottom).toInt()
            else -> MeasureSpec.getSize(heightMeasureSpec)
        }
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        //绘制上半部分
        var count = canvas.save()
        canvas.translate(paddingLeft + imageWidth / 2, paddingTop + imageWidth / 2)
        canvas.rotate(-angle)
        canvas.clipRect(
            - imageWidth,
            - imageWidth,
            imageWidth,
            0f
        )
        canvas.rotate(angle)
        canvas.translate(-(paddingLeft + imageWidth / 2), -(paddingTop + imageWidth / 2))
        canvas.drawBitmap(
            imageBitmap,
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            paint
        )
        canvas.restoreToCount(count)

        //绘制下半部分
        count = canvas.save()
        canvas.translate(paddingLeft + imageWidth / 2f, paddingTop + imageWidth / 2f)
        canvas.rotate(-angle)
        camera.applyToCanvas(canvas)
        canvas.clipRect(
            -imageWidth,
            0f,
            imageWidth,
            imageWidth
        )
        canvas.rotate(angle)
        canvas.translate(-(paddingLeft + imageWidth / 2f), -(paddingTop + imageWidth / 2f))
        canvas.drawBitmap(
            imageBitmap,
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            paint
        )
        canvas.restoreToCount(count)
    }
}