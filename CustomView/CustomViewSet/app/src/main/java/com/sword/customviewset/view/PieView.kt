package com.sword.customviewset.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class PieView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val angles = floatArrayOf(60f, 90f, 150f, 60f)
    private val colors = listOf(Color.parseColor("#C2185B"), Color.parseColor("#00ACC1"), Color.parseColor("#558B2F"), Color.parseColor("#5D4037"))
    private val radius = 200f.px
    private lateinit var rectF: RectF
    //消除锯齿
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val translation = 20f.px

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rectF = RectF(-radius/2, -radius/2, radius/2, radius/2)
    }

    override fun onDraw(canvas: Canvas?) {
        var startAngle = 0f
        //将坐标轴移到中间的位置
        canvas?.translate(width/2f, height/2f)

        //画扇形
        for((index, angle) in angles.withIndex()) {

            if(index == 1) {
                canvas?.save()
                canvas?.translate((cos(Math.toRadians(startAngle + angle.toDouble()/2)) *translation).toFloat(), (sin(Math.toRadians(startAngle + angle.toDouble()/2))*translation).toFloat())
            }

            paint.color = colors[index]
            canvas?.drawArc(rectF, startAngle, angle, true, paint)
            startAngle += angle

            if (index == 1) {
                canvas?.restore()
            }
        }

    }
}