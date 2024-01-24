package sword.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.swordlibrary.R
import sword.BitmapUtil
import sword.dp
import sword.logger.SwordLog
import sword.utils.printDebugInfo

class MaskFilterSampleView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
  private val tag = "MaskFilterSampleView"
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val bitmap = BitmapUtil.createBitmap(resources, R.drawable.batman, 150f.dp, 150f.dp)

  private val internalMargin = 60f.dp

  private val normalBlurMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL)
  private val innerBlurMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.INNER)
  private val outerBlurMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.OUTER)
  private val solidBlurMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.SOLID)

  init {
    setLayerType(LAYER_TYPE_SOFTWARE, null)
    bitmap.printDebugInfo(tag)
  }


  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(
      (paddingLeft + bitmap.width + internalMargin * 2 + paddingRight).toInt(),
      (paddingTop + (bitmap.height + internalMargin) * 4 + paddingBottom + internalMargin).toInt()
    )
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    var heightUsed = paddingTop.toFloat() + internalMargin
    val start = paddingLeft.toFloat() + internalMargin
    // 第一个：NORMAL
    paint.maskFilter = normalBlurMaskFilter
    canvas.drawBitmap(bitmap, start, heightUsed, paint)
    heightUsed += bitmap.height + internalMargin

    // 第二个：INNER
    paint.maskFilter = innerBlurMaskFilter
    canvas.drawBitmap(bitmap, start, heightUsed, paint)
    heightUsed += bitmap.height + internalMargin

    // 第三个：OUTER
    paint.maskFilter = outerBlurMaskFilter
    canvas.drawBitmap(bitmap, start, heightUsed, paint)
    heightUsed += bitmap.height + internalMargin

    // 第四个：SOLID
    paint.maskFilter = solidBlurMaskFilter
    canvas.drawBitmap(bitmap, start, heightUsed, paint)
  }
}