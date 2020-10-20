package com.sword.customviewset.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi

class DashBordView (context: Context) : View(context) {
    private val OPEN_ANGLE: Int = 60
    private var paint = Paint()
    private val sweepAngle: Float = 360f - OPEN_ANGLE
    private val startAngle: Float = OPEN_ANGLE / 2f + 90
    private val path = Path()
    private val phaseCount = 20
    private lateinit var effect: PathDashPathEffect
    private val pointerLength = 10
    private val redius = 100
    val curValue = 5


    init {
        paint.style = Paint.Style.STROKE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        path.reset()
        path.addArc(-redius, -redius, redius, redius, startAngle, sweepAngle)
        effect = PathDashPathEffect(path, 0f, caculatePhase(), PathDashPathEffect.Style.ROTATE)
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
        canvas.drawLine(0, 0, )
    }

    private fun caculatePhase(): Float = sweepAngle/(phaseCount - 1)
}