package sword.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import sword.logger.SwordLog
import sword.dp2px

class TextDisplayView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val tag = "TextDisplayView"
    private val ringWidth = dp2px(10f)
    private val radius = dp2px(100f)
    private val ringBackgroundColor = Color.rgb(144,158, 167)
    private val ringForegroundColor = Color.rgb(235, 83, 129)
    private val centerText = "abab"
    private val startAngle = -90f
    private val sweepAngle = 210f
    private val textBounds = Rect()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = ringWidth
        color = ringBackgroundColor
        textSize = dp2px(70f)
    }
    private val ringRect = RectF(
        paddingLeft + paint.strokeWidth,
        paddingTop + paint.strokeWidth,
        paddingLeft + paint.strokeWidth + radius * 2,
        paddingTop + paint.strokeWidth + radius * 2
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            (paddingLeft + (paint.strokeWidth + radius) * 2 + paddingRight + 2).toInt(),
            (paddingTop + (paint.strokeWidth + radius) * 2 + paddingBottom + 2).toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {
        //绘制圆环
        canvas.drawOval(ringRect, paint)
        paint.color = ringForegroundColor
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(ringRect, startAngle, sweepAngle, false, paint)

        //绘制文字
        paint.style = Paint.Style.FILL
        paint.getTextBounds(centerText, 0, centerText.length, textBounds)
        SwordLog.debug(tag, "textBounds left: ${textBounds.left}, top: ${textBounds.top}, right: ${textBounds.right}, bottom: ${textBounds.bottom}")
        val textStartX = ringRect.left + radius - (textBounds.left + textBounds.right) / 2
        val textStartY = ringRect.top + radius - (textBounds.top + textBounds.bottom) / 2
        canvas.drawText(centerText, textStartX, textStartY, paint)
    }
}