package sword.view

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import sword.dp
import sword.dp2px

class PointView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    var point = PointF(paddingLeft.toFloat(), paddingTop.toFloat())
        set(value) {
            field = value
            invalidate()
        }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00796B")
        style = Paint.Style.FILL
        strokeWidth = dp2px(40f)
        strokeCap = Paint.Cap.ROUND
    }

    private val pointAnimator =
        ObjectAnimator.ofObject(this, "point", PointTypeEvaluator(), PointF(100f, 400f)).apply {
            duration = 2000
            startDelay = 700
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }

    init {
        setPadding(dp(5), dp(5), 0, 0)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPoint(paddingLeft + point.x + paint.strokeWidth/2, paddingRight + point.y + paint.strokeWidth/2, paint)
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