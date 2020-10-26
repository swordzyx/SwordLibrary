package com.sword.customviewset.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.cos
import kotlin.math.sin

class DashBordView (context: Context, attribute: AttributeSet) : View(context, attribute) {
    private val OPEN_ANGLE: Int = 60
    //传入 Paint.ANTI_ALIAS_FLAG 表示消除锯齿
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val sweepAngle: Float = 360f - OPEN_ANGLE
    private val startAngle: Float = OPEN_ANGLE / 2f + 90
    private val path = Path()
    private val phaseCount = 20
    private lateinit var effect: PathDashPathEffect
    private val pointerLength = 80f.px
    private val redius = 100f.px
    private val dash = Path()
    private val DASH_WIDTH = 2f.px
    private val DASH_LENGTH = 10f.px
    val curValue = 5


    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f.px
        //CCW 为逆时针的意思
        dash.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        path.reset()

        //往路径中添加一个弧
        path.addArc(-redius, -redius, redius, redius, startAngle, sweepAngle)
        val pathMesure = PathMeasure(path, false)

        //添加一个特效
        effect = PathDashPathEffect(dash, (pathMesure.length-DASH_WIDTH)/(phaseCount), 0f, PathDashPathEffect.Style.ROTATE)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //画弧
        canvas?.translate(width/2f, height/2f)
        canvas?.drawPath(path, paint)

        //画刻度
        paint.pathEffect = effect
        canvas?.drawPath(path, paint)
        paint.pathEffect = null

        //画指针
        canvas?.drawLine(0f, 0F, pointerLength*cos(calculatePointerRadians()).toFloat(),
                pointerLength*sin(calculatePointerRadians()).toFloat(), paint)
    }

    //当前指针的弧度
    private fun calculatePointerRadians() = Math.toRadians((startAngle + (curValue) * (sweepAngle/20f)).toDouble())



}