package sword.view

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Style
import android.util.AttributeSet
import android.view.View
import com.example.swordlibrary.R
import sword.BitmapUtil
import sword.logger.SwordLog
import sword.dp
import sword.dp2px
import kotlin.math.max

class XfermodeViewSampleView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val tag = "CircleXfermodeView"

    private val radius = 100.dp
    private val porterDuffXfermodeSrcIn = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private val porterDuffXfermodeSrc = PorterDuffXfermode(PorterDuff.Mode.SRC)
    private val porterDuffXfermodeDstIn = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    private val porterDuffXfermodeDstOut = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        strokeWidth = dp2px(8f)
    }

    //边上需要预留出轮廓的绘制空间
    private val bounds = RectF(
        paddingLeft.toFloat() + paint.strokeWidth,
        paddingTop.toFloat() + paint.strokeWidth,
        paddingLeft.toFloat() + radius * 2,
        paddingTop.toFloat() + radius * 2
    )
    private val internalMargin = 2f.dp

    private val rengwuxianLogoBitmap = BitmapUtil.createBitmap(resources, R.drawable.avatar_rengwuxian, radius * 2f, radius * 2f)
    private val batmanBitmap = BitmapFactory.decodeResource(resources, R.drawable.batman)
    private val batmapLogoBitmap = BitmapFactory.decodeResource(resources, R.drawable.batman_logo)
    private val batmanCount = 3

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        SwordLog.debug(tag, "strokeWidth: ${paint.strokeWidth}")

        val width = max(
            (radius + paint.strokeWidth.toInt()) * 2,
            batmanBitmap.width
        )
        val rengwuxianHeight = (radius + paint.strokeWidth.toInt()) * 2 + internalMargin
        val batmanHeight = batmanBitmap.height * batmanCount + internalMargin * batmanCount

        setMeasuredDimension(
            paddingLeft + width + paddingRight,
            (paddingTop + rengwuxianHeight + batmanHeight + paddingBottom).toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {
        var heightUsed = paddingTop.toFloat()
        canvas.drawOval(bounds, paint)
        rengwuxianLogoBitmap.let {
            paint.style = Style.FILL
            val count = canvas.saveLayer(bounds, paint)
            //绘制圆
            canvas.drawOval(
                bounds,
                paint
            )
            paint.xfermode = porterDuffXfermodeSrcIn
            canvas.drawBitmap(it, paddingLeft.toFloat(), paddingTop.toFloat(), paint)
            paint.xfermode = null
            canvas.restoreToCount(count)
        }
        heightUsed += bounds.bottom + paint.strokeWidth + internalMargin

        val batmanLogoHoriOffset = (batmanBitmap.width - batmapLogoBitmap.width) / 2f
        val batmanLogoVerticalOffset = (batmanBitmap.height - batmapLogoBitmap.height) / 2f

        bounds.set(paddingLeft.toFloat(), heightUsed, (paddingLeft + batmanBitmap.width).toFloat(), heightUsed + batmanBitmap.height)
        var count = canvas.saveLayer(bounds, paint)
        canvas.drawBitmap(batmanBitmap, paddingLeft.toFloat(), heightUsed, paint)
        paint.xfermode = porterDuffXfermodeSrc
        canvas.drawBitmap(batmapLogoBitmap,
            bounds.left + batmanLogoHoriOffset,
            heightUsed + batmanLogoVerticalOffset,
            paint)
        paint.xfermode = null
        canvas.restoreToCount(count)
        heightUsed += batmanBitmap.height + internalMargin


        bounds.set(paddingLeft.toFloat(), heightUsed, (paddingLeft + batmanBitmap.width).toFloat(), heightUsed + batmanBitmap.height)
        count = canvas.saveLayer(bounds, paint)
        canvas.drawBitmap(batmanBitmap, paddingLeft.toFloat(), heightUsed, paint)
        paint.xfermode = porterDuffXfermodeDstIn
        canvas.drawBitmap(batmapLogoBitmap,
            bounds.left + batmanLogoHoriOffset,
            heightUsed + batmanLogoVerticalOffset,
            paint)
        paint.xfermode = null
        canvas.restoreToCount(count)
        heightUsed += batmanBitmap.height + internalMargin


        bounds.set(paddingLeft.toFloat(), heightUsed, (paddingLeft + batmanBitmap.width).toFloat(), heightUsed + batmanBitmap.height)
        count = canvas.saveLayer(bounds, paint)
        canvas.drawBitmap(batmanBitmap, paddingLeft.toFloat(), heightUsed, paint)
        paint.xfermode = porterDuffXfermodeDstOut
        canvas.drawBitmap(batmapLogoBitmap,
            bounds.left + batmanLogoHoriOffset,
            heightUsed + batmanLogoVerticalOffset,
            paint)
        paint.xfermode = null
        canvas.restoreToCount(count)
        heightUsed += batmanBitmap.height + internalMargin
    }
}