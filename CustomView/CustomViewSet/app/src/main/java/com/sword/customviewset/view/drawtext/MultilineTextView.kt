package com.sword.customviewset.view.drawtext

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sword.customviewset.R
import com.sword.customviewset.view.dp

class MultilineTextView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 15.dp
        textAlign = Paint.Align.LEFT
    }
    val fontMetrics = Paint.FontMetrics()
    private var count = 0
    private var start = 0
    private val IMAGE_SIZE = 70.dp
    private val IMAGE_PADDING = 50.dp
    private val bitmap = getBitmap(IMAGE_SIZE.dp)
//    private val rect = Rect()
    var verticalOffset = 0f
    var textWidth: Float = 0.0f


    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, width - bitmap.width.toFloat(), IMAGE_PADDING, paint)

        paint.getFontMetrics(fontMetrics)
        verticalOffset = - fontMetrics.top
//        paint.getTextBounds(text, 0, text.length, rect)
//        var verticalOffset = -rect.top.toFloat()

        while(start < text.length) {
            if ((verticalOffset+fontMetrics.bottom) >= IMAGE_PADDING && (verticalOffset + fontMetrics.top) <= (IMAGE_PADDING + bitmap.height)){
                textWidth = (width - bitmap.width.toFloat())
            } else {
                textWidth = width.toFloat()
            }
            count = paint.breakText(text, start, text.length, true, textWidth, floatArrayOf(0f))
            canvas.drawText(text, start, start + count, 0f, verticalOffset, paint)
            verticalOffset += paint.fontSpacing
            start += count
        }

    }

    private fun getBitmap(width: Float, resId: Int = R.mipmap.psb): Bitmap {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, option)
        option.inJustDecodeBounds = false
        option.inDensity = option.outWidth
        option.inTargetDensity = width.toInt()
        return BitmapFactory.decodeResource(resources, resId, option)
    }
}