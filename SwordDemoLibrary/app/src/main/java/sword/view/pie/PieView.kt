package sword.view.pie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import sword.logger.SwordLog
import sword.dp
import sword.utils.Colors
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * 饼图
 */
class PieView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
    private val tag = "PieView"
    private val selectableColors = Colors.allColorsList()
    private val angles = floatArrayOf(77.87238f, 136.67133f, 39.114487f, 106.341774f)
    private val pieRenderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
    }
    
    
    private val arcRadius = 498.75
    private val sliceSpace = 3f.dp
    private val piePath = Path()
    private val pieBoundRectF = RectF()
    private val selectedArc = 1
    private val selectedArcOffset = 15.dp

    init {
        setPadding(paddingLeft + selectedArcOffset, paddingTop + selectedArcOffset, paddingRight + selectedArcOffset, paddingBottom + selectedArcOffset)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            (paddingLeft + 2 * arcRadius + paddingRight).toInt(),
            (paddingTop + 2 * arcRadius + paddingBottom).toInt()
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        SwordLog.debug(tag, "width: $w, height: $h")
        SwordLog.debug(tag, "onSizeChanged left: $left, top: $top, right: $right, bottom: $bottom")
        pieBoundRectF.set(paddingLeft.toFloat(), paddingLeft.toFloat(),
            (paddingLeft.toFloat() + 2 * arcRadius).toFloat(),
            (paddingTop + 2f * arcRadius).toFloat())
        
    }

    override fun onDraw(canvas: Canvas) {
        SwordLog.debug(tag, "left: $left, top: $top, right: $right, bottom: $bottom")
        //绘制扇形
        var startAngle: Float
        var drawedAngle = 0f
        
        angles.forEachIndexed { index, angle ->
            pieRenderPaint.color = color(index)
            
            startAngle = drawedAngle + sliceSpace / 2f
            val sweepAngle = angle - sliceSpace
            val middleArcRadian = (startAngle + sweepAngle / 2) * (Math.PI / 180)
            val dx = cos(middleArcRadian).toFloat() * selectedArcOffset
            val dy = sin(middleArcRadian).toFloat() * selectedArcOffset
            
            /*if (index == selectedArc) {
                SwordLog.debug("dx: $dx, dy: $dy")
                canvas.translate(dx, dy)
            }*/
            piePath.reset()
            //绘制弧形
            piePath.arcTo(pieBoundRectF, startAngle, sweepAngle)
            
            val centerX = pieBoundRectF.centerX()
            val centerY = pieBoundRectF.centerY()
            
            val offsetForSiliceAngle = selectedArcOffset
            val lineEndX = centerX + cos(middleArcRadian) * offsetForSiliceAngle
            val lineEndY = centerY + sin(middleArcRadian) * 
            piePath.lineTo()
            
            /*if (index == selectedArc) {
                canvas.translate(-dx, -dy)
            }*/
            drawedAngle += angle
            piePath.close()
            canvas.drawPath(piePath, pieRenderPaint)
        }
    }
    
    private fun culcate

    private fun randomColor(): Int {
        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Color.valueOf(Random.nextInt(255).toFloat(), Random.nextInt(255).toFloat(),Random.nextInt(255).toFloat()).toArgb()
        } else {
            Color.rgb(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
        }

        return color
    }
    
    private fun color(index: Int): Int {
        return selectableColors[index % selectableColors.size]
    }
}