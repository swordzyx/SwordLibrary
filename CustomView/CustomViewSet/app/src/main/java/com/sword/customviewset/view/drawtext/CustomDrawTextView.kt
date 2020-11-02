package com.sword.customviewset.view.drawtext

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.sword.customviewset.R
import com.sword.customviewset.view.dp

private val CIRCLE_COLOR = Color.parseColor("#90A4AE")
private val HIGHLIGHT_COLOR = Color.parseColor("#FF4081")
private val RING_WIDTH = 20.dp
private val RADIUS = 150.dp

class CustomDrawTextView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 100.dp
        typeface = ResourcesCompat.getFont(context, R.font.font)
        textAlign = Paint.Align.CENTER
    }

    private val fontMetrics = Paint.FontMetrics()
    private val rect = Rect()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        canvas.translate((width/2).toFloat(), (height/2).toFloat())

        //绘制环
        paint.style = Paint.Style.STROKE
        paint.color = CIRCLE_COLOR
        paint.strokeWidth = RING_WIDTH
        canvas.drawCircle(0F, 0F, RADIUS, paint)

        //绘制进度条
        paint.color = HIGHLIGHT_COLOR
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(-RADIUS, -RADIUS, RADIUS, RADIUS, -90f, 225f, false, paint)

        //绘制文字 居中
        paint.style = Paint.Style.FILL
        //参数：文本， 基准线 x 值， 基准线 y 值，画笔
        paint.getFontMetrics(fontMetrics)
        //-(fontMetrics.ascent + fontMetrics.descent) / 2
        canvas.drawText("abac", 0f, 0f, paint)

        //绘制文字
        canvas.translate((-width/2).toFloat(), (-height/2).toFloat())
        paint.textSize = 150.dp
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds("abab", 0, "abab".length, rect)
        canvas.drawText("abab", (-rect.left).toFloat(), (-rect.top).toFloat(), paint)

        paint.textSize = 15.dp
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds("abab", 0, "abab".length, rect)
        canvas.drawText("abab", (-rect.left).toFloat(), (-rect.top).toFloat(), paint)

    }
}