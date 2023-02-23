package com.example.swordlibrary.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Size
import android.view.View
import com.sword.dp
import com.sword.dp2px
import kotlin.math.cos
import kotlin.math.sin


/**
 * 扔物线【自定义 View 绘制：图形位置和尺寸测量】作业
 *
 * 仪表盘
 */
class DashBoardView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val angle = 90
    private val radius = dp2px(100f)
    private val strokeWidth = dp2px(2f)
    private val dashWidth = dp2px(2f)
    private val dashLength = dp2px(5f)

    private val dashPath = Path().apply {
        addRect(RectF(0f, 0f, dashWidth, dashLength), Path.Direction.CCW)
    }

        /**
     * 刻度数量
     */
    private val dashCount = 21

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@DashBoardView.strokeWidth
        color = Color.BLACK
    }

    private lateinit var arcPath: Path
    private lateinit var pathDashPathEffect: PathDashPathEffect


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        arcPath = Path().apply {
            val centerX = width / 2
            val centerY = height / 2
            addArc(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius, 90f + angle/2f, 360f - angle)
        }

        val advance = (PathMeasure(arcPath, false).length) / (dashCount - 1)
        pathDashPathEffect = PathDashPathEffect(dashPath, advance, 0f, PathDashPathEffect.Style.ROTATE)
    }

    override fun onDraw(canvas: Canvas) {
        //绘制弧形
        paint.strokeWidth = strokeWidth
        canvas.drawPath(arcPath, paint)

        //绘制刻度
        paint.pathEffect = pathDashPathEffect
        paint.strokeWidth = dashWidth
        canvas.drawPath(arcPath, paint)
        paint.pathEffect = null


        //绘制指针
        paint.strokeWidth = strokeWidth
        val point = computePointerCoordinate(5)
        canvas.drawLine(width/2f, height/2f, point[0], point[1], paint)
    }

    /**
     * [dashIndex] 直针指向的刻度的索引，从 0 开始
     */
    private fun computePointerCoordinate(dashIndex: Int): List<Float> {
        //通过刻度计算出角度
        val anglePerDash = ((360 - angle) / (dashCount - 1))
        val dashAngle = (anglePerDash * dashIndex) + (90 + angle / 2f)

        //通过角度计算出直针的终点坐标
        val pointerLength = radius - dp(4)
        return listOf(width / 2 + cos(dashAngle) * pointerLength, height / 2 + sin(dashAngle) * pointerLength)
    }
}