package sword.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sword.dp
import com.sword.dp2px

class CircleImageView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
    private val radius = dp2px(100f)
    private val padding = dp(100)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = paddingLeft + (padding + radius) * 2 + paddingRight
        val h = paddingTop + (padding + radius) * 2 + paddingBottom
        val width = resolveSize(w.toInt(), widthMeasureSpec)
        val height = resolveSize(h.toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height/2f, radius, paint)
    }
}