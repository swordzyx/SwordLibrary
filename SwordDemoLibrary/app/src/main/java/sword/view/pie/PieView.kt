package sword.view.pie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import sword.PI_DIV_180
import sword.angleToRadian
import sword.dp
import sword.logger.SwordLog
import sword.utils.Colors
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan
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
    
    private val arcRadius = 100f.dp
    private val sliceAngle = 0.5f.dp
    private val piePath = Path()
    private val pieBoundRectF = RectF()
    private val centerPoint = PointF()
    private val selectedArcOffset = 15.dp
    private val selectArcIndex = 2
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            (paddingLeft + 2 * arcRadius + paddingRight + (selectedArcOffset shl 1)).toInt(),
            (paddingTop + 2 * arcRadius + paddingBottom + (selectedArcOffset shl 1)).toInt()
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        SwordLog.debug(tag, "width: $w, height: $h")
        SwordLog.debug(tag, "onSizeChanged left: $left, top: $top, right: $right, bottom: $bottom")
        pieBoundRectF.set(
            (paddingLeft + selectedArcOffset).toFloat(),
            (paddingTop + selectedArcOffset).toFloat(),
            (paddingLeft + selectedArcOffset + 2 * arcRadius),
            (paddingTop + selectedArcOffset + 2 * arcRadius)
        )
        centerPoint.set(pieBoundRectF.centerX(), pieBoundRectF.centerY())
    }

    
    override fun onDraw(canvas: Canvas) {
        SwordLog.debug(tag, "left: $left, top: $top, right: $right, bottom: $bottom")
        //绘制扇形
        var startAngle: Float
        var drawedAngle = 0f
        
        angles.forEachIndexed { index, angle ->
            pieRenderPaint.color = color(index)
            
            startAngle = drawedAngle + sliceAngle / 2f
            val sweepAngle = angle - sliceAngle
            val middleArcRadian = (startAngle + sweepAngle / 2) * (Math.PI / 180)
            
            piePath.reset()
            
            val centerX = pieBoundRectF.centerX()
            val centerY = pieBoundRectF.centerY()
            //计算扇形需要偏移的距离
            val offsetForSliceAngle = culcateArcCenterOffset(pieBoundRectF, startAngle, sweepAngle, arcRadius, sliceAngle)
            var lineEndX = centerX + cos(middleArcRadian) * offsetForSliceAngle
            var lineEndY = centerY + sin(middleArcRadian) * offsetForSliceAngle
            
            //绘制弧形
            if (index == selectArcIndex) {
                val offsetX = selectedArcOffset * cos(middleArcRadian)
                val offsetY = selectedArcOffset * sin(middleArcRadian)
                piePath.arcTo(
                    (pieBoundRectF.left + offsetX).toFloat(),
                    (pieBoundRectF.top + offsetY).toFloat(),
                    (pieBoundRectF.right + offsetX).toFloat(),
                    (pieBoundRectF.bottom + offsetY).toFloat(),
                    startAngle,
                    sweepAngle,
                    false)
                
                lineEndX += offsetX
                lineEndY += offsetY
            } else {
                piePath.arcTo(pieBoundRectF, startAngle, sweepAngle)
            }
            piePath.lineTo(lineEndX.toFloat(), lineEndY.toFloat())
            
            drawedAngle += angle
            piePath.close()
            canvas.drawPath(piePath, pieRenderPaint)
        }
    }
    
    private fun culcateArcCenterOffset(arcBoundRectF: RectF, startAngle: Float, sweepAngle: Float, radius: Float, sliceAngle: Float): Float {
        val centerX = arcBoundRectF.centerX()
        val centerY = arcBoundRectF.centerY()
        SwordLog.debug(tag, "圆心坐标: ($centerX, $centerY)")
        
        val radianStartAngle = angleToRadian(startAngle)
        val arcStartX = centerX + cos(radianStartAngle) * radius
        val arcStartY = centerY + sin(radianStartAngle) * radius
        SwordLog.debug(tag, "弧起点坐标: ($arcStartX, $arcStartY)")
        
        val radianEndAngle = angleToRadian(startAngle + sweepAngle)
        val arcEndX = centerX + cos(radianEndAngle) * radius
        val arcEndY = centerY + sin(radianEndAngle) * radius
        SwordLog.debug(tag, "弧终点坐标: ($arcEndX, $arcEndY)")
        
        val distanceFromStartToEnd = sqrt(
            (arcEndX - arcStartX).toDouble().pow(2.0) + 
            (arcEndY - arcStartY).toDouble().pow(2.0)
        )
        SwordLog.debug(tag, "弧终点到起点的直线距离: $distanceFromStartToEnd")
        
        val radianHalfSweepAngle = angleToRadian((sweepAngle + sliceAngle) / 2f)
        val radiusMiddlePart = distanceFromStartToEnd / 2f / tan(radianHalfSweepAngle)
        SwordLog.debug(tag, "半径分成三段后，第二段长度: $distanceFromStartToEnd")
        
        val radianMiddleArcAngle = angleToRadian(startAngle + sweepAngle / 2f)
        val arcMiddleX = centerX + cos(radianMiddleArcAngle) * radius
        val arcMiddleY = centerY + sin(radianMiddleArcAngle) * radius
        SwordLog.debug(tag, "弧段中坐标: $distanceFromStartToEnd")
        
        val lineMiddleX = (arcStartX + arcEndX) / 2f
        val lineMiddleY = (arcStartY + arcEndY) / 2f
        val radiusThirdPart = sqrt(
            (arcMiddleX - lineMiddleX).toDouble().pow(2) +
                (arcMiddleY - lineMiddleY).toDouble().pow(2)
        )
        SwordLog.debug(tag, "半径分成三段后，第三段长度: : $radiusThirdPart")
        
        val result = (arcRadius - radiusMiddlePart - radiusThirdPart).toFloat()
        SwordLog.debug(tag, "扇形需要偏移的距离: $result")
        return result
    }
    

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