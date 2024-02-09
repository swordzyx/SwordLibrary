package sword.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import sword.dp
import sword.logger.SwordLog

/**
 * https://rengwuxian.com/ui-1-3/ 练习代码
 * todo：未运行
 */
class DrawTextSample(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
  private val tag = "DrawTextSample"
  private val internalPadding = 5f.dp

  //使用 drawText 方法绘制文字
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    textSize = 60f
  }
  private val text = "Hello HenCode"
  private val textBounds = Rect()
  private val fontMetric = FontMetrics()
  private var textMeasureWidth = 0f

  //使用 StaticLayout 绘制文字
  private val textPaint = TextPaint()
  private val staticLayoutText = "Hello\nHenCode"
  private val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    StaticLayout.Builder.obtain(staticLayoutText, 0, staticLayoutText.length, textPaint, width).build()
  } else {
    StaticLayout(staticLayoutText, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
  }

  //typeface 示例
  private val typeface = Typeface.createFromAsset(context.assets, "Satisfy-Regular.ttf")

  //measureText 示例
  private val measurePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    textSize = 120f
    color = Color.parseColor("#E91E63")
  }
  private val measureTextBounds = Rect()
  private val text1 = "三个月内你胖了"
  private val text2 = "4.5"
  private val text3 = "公斤"

  //fontMetrics 示例
  private val fontMetricsPaint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeWidth = 20f
    color = Color.parseColor("#E91E63")
  }
  private val fontMetricsPaint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    textSize = 160f
  }
  private val texts = arrayOf("A", "a", "J", "j", "Â", "â")
  private val fontMetricsTextBounds = Rect()


  init {
    paint.getTextBounds(text, 0, text.length, textBounds)
    paint.getFontMetrics(fontMetric)

    textMeasureWidth = paint.measureText(text)
    SwordLog.debug(tag, "textBounds: $textBounds, paint.measureText 返回值：$textMeasureWidth, 系统推荐的间距为：${paint.fontSpacing}, ascent：${fontMetric.ascent}，descent：${fontMetric.descent}, top: ${fontMetric.top}, bottom: ${fontMetric.bottom}, leading: ${fontMetric.leading}")
  }
  override fun onDraw(canvas: Canvas) {
    var top = paddingTop - fontMetric.top
    var left = paddingLeft + (width - textMeasureWidth) / 2f
    //绘制文字
    canvas.drawText(text, top, left, paint)
    top += fontMetric.bottom + internalPadding

    //使用 StaticLayout 绘制文字
    canvas.translate(0f, top)
    staticLayout.draw(canvas)
    canvas.translate(0f, -top)
    top += fontMetric.bottom - fontMetric.top + internalPadding

    //setTextSize 示例
    paint.textSize = 16f
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    paint.textSize = 24f
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding
    paint.textSize = 48f
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding
    paint.textSize = 72f
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding

    //还原 paint 的设置
    paint.textSize = 60f
    paint.getTextBounds(text, 0, text.length, textBounds)

    //Typeface 示例
    paint.typeface = null
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding

    paint.typeface = Typeface.SERIF
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding

    paint.typeface = typeface
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding

    //恢复 paint 初始设置
    paint.typeface = null

    //FakeBold 示例
    paint.isFakeBoldText = true
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding

    //删除线示例
    paint.isStrikeThruText = true
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding
    //还原 paint 的配置
    paint.isStrikeThruText = false

    //下划线示例
    paint.isUnderlineText = true
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding
    //还原 paint 配置
    paint.isUnderlineText = false

    //横向错切示例
    paint.textSkewX = -0.5f
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding
    //还原 paint 配置
    paint.textSkewX = 0f

    //文字横向缩放示例
    paint.textScaleX = 1f
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding
    paint.textScaleX = 0.8f
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding
    paint.textScaleX = 1.2f
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding
    //还原 paint 配置
    paint.textScaleX = 1.0f

    //文字对齐方式示例
    paint.textAlign = Paint.Align.CENTER
    paint.getTextBounds(text, 0, text.length, textBounds)
    top -= fontMetric.top
    canvas.drawText(text, left, top, paint)
    top += fontMetric.bottom + internalPadding
    //还原 paint 配置
    paint.textAlign = Paint.Align.LEFT

    //使用 measureText 在同一行绘制多个文本
    measurePaint.getTextBounds(text2, 0, text2.length, measureTextBounds)
    top -= measureTextBounds.top
    canvas.drawText(text1, left, top, paint)
    var temp = paint.measureText(text1)
    canvas.drawText(text2, left + temp, top, measurePaint)
    temp += measurePaint.measureText(text2)
    canvas.drawText(text3, left + temp, top, paint)
    top += measureTextBounds.bottom + internalPadding


    //使用 fontMetrics 实现文字的居中显示，相比 getTextBounds，使用 fontMetrics 可以实现文字居中显示的同时，让文字的基准线对齐。
    fontMetricsPaint2.getTextBounds("A", 0, 1, fontMetricsTextBounds)
    val fontMetricsBottom = top + 250f
    canvas.drawRect(left, top,
      (width - paddingRight).toFloat(),
      fontMetricsBottom,
      fontMetricsPaint1)
    //计算文字居中显示需要往下偏移的量
    val middle = (top + fontMetricsBottom) / 2f - (fontMetric.top + fontMetric.bottom) / 2f
    canvas.drawText(texts[0], left, middle, fontMetricsPaint2)
    left += 100f
    canvas.drawText(texts[1], left, middle, fontMetricsPaint2)
    left += 100f
    canvas.drawText(texts[2], left, middle, fontMetricsPaint2)
    left += 100f
    canvas.drawText(texts[3], left, middle, fontMetricsPaint2)
    left += 100f
    canvas.drawText(texts[4], left, middle, fontMetricsPaint2)
    left += 100f
    canvas.drawText(texts[5], left, middle, fontMetricsPaint2)
  }

}