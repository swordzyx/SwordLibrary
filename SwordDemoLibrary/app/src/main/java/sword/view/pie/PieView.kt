package sword.view.pie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import sword.angleToRadian
import sword.dp
import sword.logger.SwordLog
import sword.utils.Colors
import sword.utils.toPercent
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
    private val pieDataSet = PieDataSet()

    private val selectableColors = Colors.allColorsList()
    private val angles = floatArrayOf(77.87238f, 136.67133f, 39.114487f, 106.341774f)
    private val angleSum = angles.sum()

    private val pieRenderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.FILL_AND_STROKE
    }
    private val sliceValueLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        strokeWidth = pieDataSet.valueLineWidth
        color = Color.BLACK
        textSize = 10f.dp
    }
    
    private val arcRadius = 100f.dp
    private val sliceAngle = 0.5f.dp
    private val piePath = Path()
    private val pieBoundRectF = RectF()
    private val centerPoint = PointF()
    private val selectedArcOffset = 15.dp
    private val selectArcIndex = 2
    private val valueOffset = 5f.dp
    private var valueTextHeight: Float = 0f

    init {
        val textBounds = Rect()
        sliceValueLinePaint.getTextBounds("Q", 0, 1, textBounds)
        valueTextHeight = (textBounds.top + textBounds.bottom) / 2f
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //饼图宽度 + 线的宽度 + 文字的宽度
        setMeasuredDimension(
            (paddingLeft + 3 * arcRadius + paddingRight + (selectedArcOffset shl 1)).toInt(),
            (paddingTop + 3 * arcRadius + paddingBottom + (selectedArcOffset shl 1)).toInt()
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        SwordLog.debug(tag, "width: $w, height: $h")
        SwordLog.debug(tag, "onSizeChanged left: $left, top: $top, right: $right, bottom: $bottom")
        pieBoundRectF.set(
            (paddingLeft + selectedArcOffset + arcRadius / 2),
            (paddingTop + selectedArcOffset + arcRadius / 2),
            (paddingLeft + selectedArcOffset + 2.5f * arcRadius),
            (paddingTop + selectedArcOffset + 2.5f * arcRadius)
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
            //计算弧度
            val middleAngle = startAngle + sweepAngle / 2
            val middleArcRadian = middleAngle * (Math.PI / 180)
            
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

            drawValue(canvas, ((angle / angleSum) * 100).toPercent(), middleArcRadian.toFloat(), middleAngle)
        }
    }

    private fun drawValue(canvas: Canvas, value: String, valuePositionRadian: Float, valuePositionAngle: Float) {
        SwordLog.debug(tag, "drawValue >> value: $value, valuePositionRadian: $valuePositionRadian, valuePositionAngle: $valuePositionAngle")
        //1. 绘制线条，线条分两段，计算每一段线条的起点和终点，lineRadius 为线段的起点距圆心的距离
        val lineRadius = arcRadius * (pieDataSet.valueLinePart1OffsetPercentage / 100)
        val pt0x = centerPoint.x + cos(valuePositionRadian) * lineRadius
        val pt0y = centerPoint.y + sin(valuePositionRadian) * lineRadius

        val pt1x = centerPoint.x + cos(valuePositionRadian) * (lineRadius + lineRadius * pieDataSet.valueLinePart1Percent)
        val pt1y = centerPoint.y + sin(valuePositionRadian) * (lineRadius + lineRadius * pieDataSet.valueLinePart1Percent)

        val pt2x = if (valuePositionAngle <= 90f || valuePositionAngle >= 270f ) {
            pt1x + lineRadius * pieDataSet.valueLinePart2Percent
        } else {
            pt1x - lineRadius * pieDataSet.valueLinePart2Percent
        }
        val pt2y = pt1y

        SwordLog.debug(tag, "lineRadius: $lineRadius, 中心x：${centerPoint.x}, 中心y：${centerPoint.y}, 点1：($pt0x, $pt0y), 点2：($pt1x, $pt1y), 点3：($pt2x, $pt2y)")

        canvas.drawLine(pt0x, pt0y, pt1x, pt1y, sliceValueLinePaint)
        canvas.drawLine(pt1x, pt1y, pt2x, pt2y, sliceValueLinePaint)

        //2. 绘制百分比
        val valueX = if (valuePositionAngle <= 90f || valuePositionAngle >= 270f ) {
            sliceValueLinePaint.textAlign = Paint.Align.LEFT
            pt2x + valueOffset
        } else {
            sliceValueLinePaint.textAlign = Paint.Align.RIGHT
            pt2x - valueOffset
        }
        val valueY = pt2y - valueTextHeight
        canvas.drawText(value, valueX, valueY, sliceValueLinePaint)
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