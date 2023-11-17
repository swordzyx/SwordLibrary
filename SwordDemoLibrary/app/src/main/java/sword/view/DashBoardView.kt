package sword.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import sword.logger.SwordLog
import sword.dp
import sword.dp2px
import sword.windowHeight
import sword.windowWidth
import kotlin.math.cos
import kotlin.math.sin


/**
 * 扔物线【自定义 View 绘制：图形位置和尺寸测量】作业
 *
 * 仪表盘
 */
class DashBoardView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val tag = "DashBoardView"
    private val angle = 90
    private val radius = dp2px(100f)
    private val strokeWidth = dp2px(2f)
    private val dashWidth = dp2px(2f)
    private val dashLength = dp2px(5f)

    private lateinit var arcPath: Path
    private lateinit var pathDashPathEffect: PathDashPathEffect
    private var advance = 0f
    private var arcPathLength = 0f
    private var centerX = 0f
    private var centerY = 0f

    init {
        setPadding(paddingLeft + 10.dp, paddingTop + 10.dp, paddingRight + 10.dp, paddingBottom + 10.dp)
    }

    private val dashPath = Path().apply {
        addRect(RectF(0f, 0f, dashWidth, dashLength), Path.Direction.CCW)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measureWidth = paddingLeft + 2 * radius.toInt() + paddingRight
        val measureHeight = paddingTop + 2 * radius.toInt() + paddingBottom
        SwordLog.debug(tag, "measureWidth: $measureWidth, measuredHeight: $measureHeight, screenWidth: $windowWidth, screenHeight: $windowHeight")
        setMeasuredDimension(
            measureWidth,
            measureHeight
        )
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        arcPath = Path().apply {
            centerX = paddingLeft + radius
            centerY = paddingTop + radius
            addArc(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius, 90f + angle/2f, 360f - angle)
        }

        arcPathLength = PathMeasure(arcPath, false).length
        advance = (arcPathLength - dashWidth) / (dashCount - 1)
        //advance 指画完一个刻度之后，隔多远画下一个刻度，整个周长分成 20 分，实际会有 21 个刻度，但是最后一个刻度由于已经超出了 path 的范围，没有画出来，所以间隔需要再缩小一点，缩小到正好预留出最后一个刻度的位置，就相当于整个周长分成 20 份的基础上，再减去刻度的宽度平均分到 20 份，每份的宽度，这样就能预留出最后一个刻度的位置了。
        pathDashPathEffect = PathDashPathEffect(dashPath, advance, 0f, PathDashPathEffect.Style.ROTATE)
    }

    override fun onDraw(canvas: Canvas) {
        SwordLog.debug(tag, "left: $left, top: $top, right: $right, bottom: $bottom")
        //绘制弧形
        paint.strokeWidth = strokeWidth
        canvas.drawPath(arcPath, paint)

        //绘制刻度
        paint.pathEffect = pathDashPathEffect
        paint.strokeWidth = dashWidth
        canvas.drawPath(arcPath, paint)
        paint.pathEffect = null


        //绘制指针
        paint.strokeWidth = dashWidth
        val point = computePointerCoordinate(5)
        canvas.drawLine(centerX, centerY, point[0], point[1], paint)
    }

    /**
     * [dashIndex] 直针指向的刻度的索引，从 0 开始
     */
    private fun computePointerCoordinate(dashIndex: Int): List<Float> {
        //通过刻度计算出角度
        val pointerSweepAngle = (advance * dashIndex) / arcPathLength * (360 - angle)
        val pointerAngle = ((90 + angle/2f + pointerSweepAngle) * Math.PI / 180).toFloat()

        //通过角度计算出直针的终点坐标
        val pointerLength = radius - 18.dp
        return listOf(centerX + cos(pointerAngle) * pointerLength, centerY + sin(pointerAngle) * pointerLength)
    }
}