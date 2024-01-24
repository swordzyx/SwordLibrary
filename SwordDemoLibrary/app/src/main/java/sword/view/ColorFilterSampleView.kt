package sword.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.swordlibrary.R
import sword.dp

class ColorFilterSampleView(context: Context, attributeSet: AttributeSet? = null) : View(context, attributeSet) {

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val bitmap = BitmapFactory.decodeResource(resources, R.drawable.batman)

  private val internalMargin = 2f.dp
  private val colorFilterCount = 3

  //去掉红色
  private val removeRedLightColorFilter = LightingColorFilter(0x00ffff, 0x000000)
  //增加绿色
  private val addGreenLightColorFilter = LightingColorFilter(0x00ffff, 0x003000)
  //去掉色彩饱和度，让图片变灰
  private val colorMatrixColorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
    //去掉饱和度
    setSaturation(0f)
  })

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    setMeasuredDimension(paddingLeft + bitmap.width + 100 + paddingRight,
      (paddingTop + bitmap.height * colorFilterCount + internalMargin * colorFilterCount + paddingBottom).toInt()
    )
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    var heightUsed = paddingTop.toFloat()
    paint.colorFilter = removeRedLightColorFilter
    canvas.drawBitmap(bitmap, paddingLeft.toFloat(), heightUsed, paint)
    heightUsed += bitmap.height + internalMargin

    paint.colorFilter = addGreenLightColorFilter
    canvas.drawBitmap(bitmap, paddingLeft.toFloat(), heightUsed, paint)
    heightUsed += bitmap.height + internalMargin

    paint.colorFilter = colorMatrixColorFilter
    canvas.drawBitmap(bitmap, paddingLeft.toFloat(), heightUsed, paint)
    heightUsed += bitmap.height + internalMargin
  }
}