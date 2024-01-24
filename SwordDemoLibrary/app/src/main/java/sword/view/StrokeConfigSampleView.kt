package sword.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import sword.dp
import sword.utils.Colors
import android.graphics.Path
import android.graphics.Point
import android.graphics.PointF
import androidx.core.graphics.times


class StrokeConfigSampleView(context: Context, attributeSet: AttributeSet? = null) :
  View(context, attributeSet) {
  private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    strokeWidth = 30f.dp
    color = Colors.HOLO_BLUE
    style = Paint.Style.STROKE
  }
  private val interMargin = 50f.dp

  private val lineWidth = 150f.dp
  private val strokeCapSize = PointF().apply {
    x = lineWidth + paint.strokeWidth
    y = (paint.strokeWidth + interMargin) * 3f
  }

  private val path = Path().apply {
    rLineTo(200f, 0f)
    rLineTo(-160f, 120f)
  }
  private val strokeJoinSize = PointF().apply {
    x = 900f
    y = 200f
  }

  private val strokeMiterSize = PointF().apply {
    x = 900f
    y = 200f
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    setMeasuredDimension(
      (paddingLeft + strokeCapSize.x + interMargin + strokeJoinSize.x + interMargin + strokeMiterSize.x + paddingRight).toInt(),
      (paddingTop + strokeCapSize.y + interMargin + strokeJoinSize.y + interMargin + strokeMiterSize.y + paddingTop).toInt()
    )
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    val startX = paddingLeft.toFloat() + paint.strokeWidth / 2f
    var heightUsed = paddingTop.toFloat() + paint.strokeWidth / 2f
    // 第一个：BUTT
    paint.strokeCap = Paint.Cap.BUTT
    canvas.drawLine(startX, heightUsed, startX + lineWidth, heightUsed, paint)
    heightUsed += interMargin

    paint.strokeCap = Paint.Cap.ROUND
    canvas.drawLine(startX, heightUsed, startX + lineWidth, heightUsed, paint)
    heightUsed += interMargin

    paint.strokeCap = Paint.Cap.SQUARE
    canvas.drawLine(startX, heightUsed, startX + lineWidth, heightUsed, paint)
    heightUsed += interMargin

    paint.strokeJoin = Paint.Join.MITER
    var offsetX = paddingLeft.toFloat() + paint.strokeWidth / 2f
    canvas.translate(offsetX, heightUsed)
    canvas.drawPath(path, paint)

    paint.strokeJoin = Paint.Join.BEVEL
    canvas.translate(400f, 0f)
    canvas.drawPath(path, paint)
    offsetX += 400f

    paint.strokeJoin = Paint.Join.ROUND
    canvas.translate(300f, 0f)
    canvas.drawPath(path, paint)
    offsetX += 300f
    heightUsed += strokeJoinSize.y + interMargin

    // -------------------------------------------------------------------
    paint.strokeJoin = Paint.Join.MITER
    paint.strokeMiter = 1f
    canvas.translate(-700f, strokeJoinSize.y + interMargin)
    canvas.drawPath(path, paint)
    offsetX -= 700f

    paint.strokeMiter = 2f
    canvas.translate(300f, 0f)
    canvas.drawPath(path, paint);
    offsetX += 300f

    paint.strokeMiter = 5f
    canvas.translate(300f, 0f)
    canvas.drawPath(path, paint)
    offsetX += 300f

    canvas.translate(-offsetX, -heightUsed)
  }
}