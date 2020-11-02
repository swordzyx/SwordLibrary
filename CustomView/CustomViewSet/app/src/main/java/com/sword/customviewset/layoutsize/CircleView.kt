package com.sword.customviewset.layoutsize

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.sword.customviewset.view.dp

private val RADIUS = 100.dp
private val PADDING = 50.dp

class CircleView(context: Context, attris: AttributeSet):View(context, attris) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //计算 View 的期望大小
        val size = ((PADDING + RADIUS)*2).toInt()
        //结合期望的大小和父View提供的限制条件算出测量的大小。

        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var modeStringWidth = ""
        var modeStringHeight = ""

        /*
        * layout_width , layout_height 不同的值对应以下模式
        * * * * 精确的值（例如 200dp 等）和 match_parent ：mode = EXACTLY
        * * * * wrap_content：mode = AT_MOST
        * */
        val widthMeasure = when(widthMode) {
            MeasureSpec.EXACTLY -> {
                modeStringWidth = "MeasureSpec.EXACTLY"
                widthSpecSize
            }
            MeasureSpec.AT_MOST -> {
                modeStringWidth = "MeasureSpec.AT_MOST"
                if (size > widthSpecSize) widthSpecSize else size
            }
            else -> size
        }
        val heightMeasure = when(heightMode) {
            MeasureSpec.EXACTLY -> {
                modeStringHeight = "MeasureSpec.EXACTLY"
                heightSpecSize
            }
            MeasureSpec.AT_MOST -> {
                modeStringHeight = "MeasureSpec.AT_MOST"
                if(size > heightSpecSize) heightSpecSize else size
            }
            else -> size
        }

        Log.d("Measure", "Width Mode: $modeStringWidth width: $widthSpecSize Height Mode: $modeStringHeight height: $heightSpecSize")
//        val widthMeasure = resolveSize(size, widthMeasureSpec)
//        val heightMeasure = resolveSize(size, heightMeasureSpec)
        setMeasuredDimension(widthMeasure, heightMeasure)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(PADDING + RADIUS, PADDING + RADIUS, RADIUS, paint)
    }
}