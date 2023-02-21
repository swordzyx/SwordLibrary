package com.example.swordlibrary.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sword.dp2px


/**
 * 扔物线【自定义 View 绘制：图形位置和尺寸测量】作业
 *
 * 仪表盘
 */
class DashBoardView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val angle = 90
    private val radius = dp2px(100f)
    private val strokeWidth = dp2px(2f)

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


        pathDashPathEffect = PathDashPathEffect(arcPath, )
    }

    override fun onDraw(canvas: Canvas) {
        //绘制弧形

        canvas.drawArc(
            )

        //绘制刻度


        //绘制指针

    }
}