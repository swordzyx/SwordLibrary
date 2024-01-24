package sword.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class ShadowLayerSampleView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
  val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    setShadowLayer(10f, 0f, 0f, Color.RED)
    textSize = 120f
  }
  val text = "Hello HenCoder"
  val textBound = Rect().apply {
    paint.getTextBounds(text, 0, text.length, this)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    //默认使用父 View 的宽高
    setMeasuredDimension(
      MeasureSpec.getSize(widthMeasureSpec),
      MeasureSpec.getSize(heightMeasureSpec)
    )
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.drawText(text, 50f, 200f, paint)
  }
}