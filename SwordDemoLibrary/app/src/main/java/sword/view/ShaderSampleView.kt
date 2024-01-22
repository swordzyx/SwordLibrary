package sword.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import com.example.swordlibrary.R
import sword.BitmapUtil
import sword.dp
import sword.logger.SwordLog
import kotlin.math.abs


/**
 * 着色器示例
 */
class ShaderSampleView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
  private val tag = "ShaderSampleView"
  
  private val color1 = Color.parseColor("#E91E63")
  private val color2 = Color.parseColor("#2196F3")
  

  private val textColor = Color.WHITE
  private val textBounds = Rect()
  private val textSize = 16f.dp
  private var textHeight: Float = 0f

  private val gradientRadius = 100f.dp
  private val rectHeight = 200f.dp
  private val internalMargin = 2f.dp

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)


  private val linearGradientClampText = "LinearGradient 示例"
  private var clampLinearGradient: LinearGradient? = null

  private val radialGradientText = "RadialGradient 示例"
  private var radialGradient: RadialGradient? = null

  private val sweepGradientText = "SweepGradient 示例"
  private var sweepGradient: SweepGradient? = null

  private val bitmap = BitmapFactory.decodeResource(resources, R.drawable.batman)
  private val bitmapShaderText = "BitmapShader 示例"
  private var bitmapShader: BitmapShader? = null

  private val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.batman_logo)
  private val composeShaderText = "ComposeShader 示例"
  private var composeShader: ComposeShader? = null
  
  init {
    paint.textSize = textSize
    paint.getTextBounds(linearGradientClampText, 0, linearGradientClampText.length, textBounds)
    textHeight = (abs(textBounds.top) + abs(textBounds.bottom)).toFloat()
    SwordLog.debug(tag, "textBounds left: ${textBounds.left}, top: ${textBounds.top}, right: ${textBounds.right}, bottom: ${textBounds.bottom}")
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    val height = paddingTop + paddingBottom + textHeight * 5f + rectHeight * 5f + internalMargin * 4f
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height.toInt())
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    SwordLog.debug(tag, "onSizeChanged, new size: ($w, $h), old size: ($oldw, $oldh)")

    val gradientRegionDistance = gradientRadius * 2

    //初始化 shader
    val centerX = w / 2f
    val gradientStartX = centerX - gradientRadius
    var gradientStartY = paddingTop + textHeight
    clampLinearGradient = LinearGradient(
      gradientStartX,
      gradientStartY,
      gradientStartX + gradientRegionDistance,
      gradientStartY + gradientRegionDistance,
      color1,
      color2,
      Shader.TileMode.CLAMP
    )

    gradientStartY += gradientRegionDistance + textHeight + internalMargin + gradientRadius
    //(center, gradientStartY) 为圆心
    radialGradient = RadialGradient(centerX, gradientStartY, gradientRadius, color1, color2, Shader.TileMode.MIRROR)

    gradientStartY += gradientRegionDistance + internalMargin + textHeight
    //(center, gradientStartY) 为圆心
    sweepGradient = SweepGradient(centerX, gradientStartY, color1, color2)

    //(center, gradientStartY) 为圆心
    bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    val bitmapShader2 = BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    composeShader = ComposeShader(bitmapShader!!, bitmapShader2, PorterDuff.Mode.DST_IN)
  }

  override fun onDraw(canvas: Canvas) {
    paint.color = textColor
    paint.style = Paint.Style.FILL

    val centerX = measuredWidth / 2f
    val textStartX = centerX - (textBounds.left + textBounds.right) / 2f
    var textStartY = (paddingTop - textBounds.top).toFloat()
    canvas.drawText(linearGradientClampText, textStartX, textStartY, paint)

    var heightUsed = paddingTop + textHeight
    //绘制 LinearGradient
    paint.shader = clampLinearGradient
    canvas.drawRect(
      paddingLeft.toFloat(), 
      heightUsed,
      (measuredWidth - paddingRight).toFloat(),
      heightUsed + rectHeight,
      paint
    )
    heightUsed += rectHeight + internalMargin

    //绘制 RadialGradient
    canvas.drawText(radialGradientText, textStartX, heightUsed - textBounds.top, paint)
    heightUsed += textHeight
    paint.shader = radialGradient
    canvas.drawRect(paddingLeft.toFloat(),
      heightUsed,
      (measuredWidth - paddingRight).toFloat(),
      heightUsed + rectHeight,
      paint)
    heightUsed += rectHeight + internalMargin

    //绘制 SweepGradient（扫描渐变）
    canvas.drawText(sweepGradientText, textStartX, heightUsed - textBounds.top, paint)
    heightUsed += textHeight
    paint.shader = sweepGradient
    canvas.drawRect(paddingLeft.toFloat(),
      heightUsed,
      (measuredWidth - paddingRight).toFloat(),
      heightUsed + rectHeight,
      paint)
    heightUsed += rectHeight + internalMargin

    //todo:绘制 BitmapGradient，显示异常
    canvas.drawText(bitmapShaderText, textStartX, heightUsed - textBounds.top, paint)
    heightUsed += textHeight
    var layerId = canvas.saveLayer(paddingLeft.toFloat(), heightUsed, (measuredWidth - paddingRight).toFloat(), heightUsed + rectHeight, paint)
    paint.shader = bitmapShader
    canvas.drawRect(paddingLeft.toFloat(),
      heightUsed,
      (measuredWidth - paddingRight).toFloat(),
      heightUsed + rectHeight,
      paint)
    canvas.restoreToCount(layerId)
    heightUsed += rectHeight + internalMargin

    //todo：绘制 ComposeShader，显示异常
    paint.shader = null
    canvas.drawText(composeShaderText, textStartX, heightUsed - textBounds.top, paint)
    heightUsed += textHeight
    layerId = canvas.saveLayer(paddingLeft.toFloat(), heightUsed, (measuredWidth - paddingRight).toFloat(), heightUsed + rectHeight, paint)
    paint.shader = composeShader
    canvas.drawRect(paddingLeft.toFloat(),
      heightUsed,
      (measuredWidth - paddingRight).toFloat(),
      heightUsed + rectHeight,
      paint)
    canvas.restoreToCount(layerId)
  }
}