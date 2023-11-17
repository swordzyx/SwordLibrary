package sword.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import sword.logger.SwordLog
import sword.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class PieView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val tag = "PieView"
    private val angles = floatArrayOf(0.2f, 0.3f, 0.4f, 0.1f)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
    }
    private val arcRadius = 100.dp
    private val selectedArc = 1
    private val selectedArcOffset = 15.dp

    private lateinit var arcRect: RectF

    init {
        setPadding(paddingLeft + selectedArcOffset, paddingTop + selectedArcOffset, paddingRight + selectedArcOffset, paddingBottom + selectedArcOffset)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            paddingLeft + 2 * arcRadius + paddingRight,
            paddingTop + 2 * arcRadius + paddingBottom
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        SwordLog.debug(tag, "width: $w, height: $h")
        SwordLog.debug(tag, "onSizeChanged left: $left, top: $top, right: $right, bottom: $bottom")
        arcRect = RectF(paddingLeft.toFloat(), paddingLeft.toFloat(), paddingLeft.toFloat() + 2 * arcRadius, paddingTop + 2f * arcRadius)
    }

    override fun onDraw(canvas: Canvas) {
        SwordLog.debug(tag, "left: $left, top: $top, right: $right, bottom: $bottom")
        //绘制扇形
        var startAngle = 0f
        angles.forEachIndexed { index, angle ->
            paint.color = randomColor()
            val sweepAngle = (360 * angle)
            val arcRadian = (startAngle + sweepAngle / 2) * (Math.PI / 180)
            val dx = cos(arcRadian).toFloat() * selectedArcOffset
            val dy = sin(arcRadian).toFloat() * selectedArcOffset
            if (index == selectedArc) {
                SwordLog.debug("dx: $dx, dy: $dy")
                canvas.translate(dx, dy)
            }
            canvas.drawArc(arcRect, startAngle, sweepAngle, true, paint)
            if (index == selectedArc) {
                canvas.translate(-dx, -dy)
            }
            startAngle += sweepAngle
        }
    }


    private fun randomColor(): Int {
        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Color.valueOf(Random.nextInt(255).toFloat(), Random.nextInt(255).toFloat(),Random.nextInt(255).toFloat()).toArgb()
        } else {
            Color.rgb(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
        }

        return color
    }
}