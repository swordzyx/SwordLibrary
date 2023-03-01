package com.example.swordlibrary.view

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.sword.dp
import com.sword.dp2px

class PointView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var point = PointF(paddingLeft.toFloat(), paddingTop.toFloat())
        set(value) {
            field = value
            invalidate()
        }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00796B")
        style = Paint.Style.FILL
        strokeWidth = dp2px(5f)
    }

    private val pointAnimator =
        ObjectAnimator.ofObject(this, "point", PointTypeEvaluator(), PointF(100f, 200f)).apply {
            duration = 2000
            startDelay = 1000
        }

    init {
        setPadding(dp(5), dp(5), 0, 0)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPoint(point.x, point.y, paint)
    }

    fun startAnimator() {
        pointAnimator.start()
    }

    class PointTypeEvaluator : TypeEvaluator<PointF> {
        override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
            val currentX = startValue.x + (endValue.x - startValue.x) * fraction
            val currentY = startValue.y + (endValue.y - startValue.y) * fraction
            return PointF(currentX, currentY)
        }
    }
}