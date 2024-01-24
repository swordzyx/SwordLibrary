package sword.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import sword.dp

class TextPathSampleView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    textSize = 120f
  }
  private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
  }
  private val text = "Hello Hencoder"
  private val textPath = Path()
  private val textBounds = Rect().apply {
    paint.getTextBounds(text, 0, text.length, this)
  }
  private val internalMargin = 5.dp

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    //绘制文字的 Path
    val textX = (paddingLeft - textBounds.left).toFloat()
    val textY = (paddingTop - textBounds.top).toFloat()
    val textHeight = textBounds.bottom - textBounds.top
    canvas.drawText(text, textX, textY, paint)

    canvas.translate(0f, textHeight.toFloat() + internalMargin)
    paint.getTextPath(text, 0, text.length, textX, textY, textPath)
    canvas.drawPath(textPath, pathPaint)
  }
}