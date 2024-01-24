package sword.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.SumPathEffect
import android.util.AttributeSet
import android.view.View
import sword.dp

class PathEffectSampleView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
  }
  private val internalMargin = 3f.dp

  private val size = PointF(500f, 200f)
  private val path = Path().apply {
    moveTo(50f, 100f)
    rLineTo(50f, 100f)
    rLineTo(80f, -150f)
    rLineTo(100f, 100f)
    rLineTo(70f, -120f)
    rLineTo(150f, 80f)
  }

  private val cornerPathEffect = CornerPathEffect(20f.dp)
  private val discretePathEffect = DiscretePathEffect(20f.dp, 5f.dp)
  //线段长度（20f） + 空白长度（10f） + 线段长度（5f） + 空白长度（10f）
  private val dashPathEffect = DashPathEffect(floatArrayOf(20f.dp, 10f.dp, 5f.dp, 10f.dp), 0f)
  private val sumPathEffect = SumPathEffect(dashPathEffect, discretePathEffect)
  private val composePathEffect = ComposePathEffect(dashPathEffect, discretePathEffect)

  private val pathEffectCount = 5

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    setMeasuredDimension(
      (paddingLeft + size.x + paddingRight).toInt(),
      (paddingTop + (size.y + internalMargin) * pathEffectCount + paddingBottom).toInt()
    )
  }
  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    var translateY = paddingTop.toFloat()
    paint.pathEffect = cornerPathEffect
    canvas.translate(paddingLeft.toFloat(), translateY)
    canvas.drawPath(path, paint)

    paint.pathEffect = discretePathEffect
    canvas.translate(0f, size.y + internalMargin)
    canvas.drawPath(path, paint)
    translateY += size.y + internalMargin

    paint.pathEffect = dashPathEffect
    canvas.translate(0f, size.y + internalMargin)
    canvas.drawPath(path, paint)
    translateY += size.y + internalMargin

    paint.pathEffect = sumPathEffect
    canvas.translate(0f, size.y + internalMargin)
    canvas.drawPath(path, paint)
    translateY += size.y + internalMargin

    paint.pathEffect = composePathEffect
    canvas.translate(0f, size.y + internalMargin)
    canvas.drawPath(path, paint)
    translateY += size.y + internalMargin

    canvas.translate(-paddingLeft.toFloat(), -translateY)
  }
}