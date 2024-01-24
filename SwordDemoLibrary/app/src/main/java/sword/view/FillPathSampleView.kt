package sword.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import sword.dp

class FillPathSampleView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var pathPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
  }

  private val pathCount = 3
  private val pathSize = PointF(500f, 200f)
  private var path: Path = Path().apply {
    moveTo(50f, 100f)
    rLineTo(50f, 100f)
    rLineTo(50f, -150f)
    rLineTo(100f, 100f)
    rLineTo(70f, -120f)
    rLineTo(150f, 80f)
  }
  private var path1: Path = Path()
  private var path2: Path = Path()
  private var path3: Path = Path()

  private val internalMargin = 5f.dp

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(
      (paddingLeft + (pathSize.x + internalMargin) * 2 + 85.dp + paddingRight).toInt(),
      (paddingTop + (pathSize.y.toInt() + internalMargin) * (pathCount) + 85.dp + paddingBottom).toInt()
    )
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
    paint.style = Paint.Style.FILL_AND_STROKE
    paint.strokeWidth = 0f
    canvas.drawPath(path, paint)

    canvas.translate(pathSize.x + internalMargin, 0f)
    paint.getFillPath(path, path1)
    canvas.drawPath(path1, pathPaint)
    //offsetX += pathSize.x + internalMargin

    canvas.translate(-(pathSize.x + internalMargin), pathSize.y)
    paint.style = Paint.Style.STROKE
    canvas.drawPath(path, paint)

    canvas.translate(pathSize.x + internalMargin, 0f)
    paint.getFillPath(path, path2)
    canvas.drawPath(path2, pathPaint)

    canvas.translate(-(pathSize.x + internalMargin), pathSize.y + 40f.dp)
    paint.strokeWidth = 40f.dp
    canvas.drawPath(path, paint)

    canvas.translate(pathSize.x + internalMargin, 0f)
    paint.getFillPath(path, path3)
    canvas.drawPath(path3, pathPaint)
  }

}