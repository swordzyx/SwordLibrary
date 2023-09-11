package sword.view

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Style
import android.util.AttributeSet
import android.view.View
import com.example.swordlibrary.R
import com.sword.*
import sword.SwordLog

class CircleXfermodeView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val tag = "CircleXfermodeView"
    private val radius = dp(100)
    private val proterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        strokeWidth = dp2px(8f)
    }
    private val startX = paddingLeft.toFloat()
    private val startY = paddingTop.toFloat()
    //边上需要预留出轮廓的绘制空间
    private val bounds = RectF(
        startX + paint.strokeWidth,
        startY + paint.strokeWidth,
        startX + radius * 2,
        startY + radius * 2
    )
    private var bitmap: Bitmap? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        SwordLog.debug(tag, "strokeWidth: ${paint.strokeWidth}")
        setMeasuredDimension(
            paddingLeft + (radius + paint.strokeWidth.toInt() + 5) * 2 + paddingRight,
            paddingTop + (radius + paint.strokeWidth.toInt() + 5) * 2 + paddingBottom
        )
    }

    init {
        setBitmap(R.drawable.avatar_rengwuxian)
    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    fun setBitmap(resId: Int) {
        setBitmap(createBitmap1(resources, resId, radius * 2f, radius * 2f))
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawOval(bounds, paint)

        //绘制图片
        bitmap?.let {
            paint.style = Style.FILL
            val count = canvas.saveLayer(bounds, paint)
            //绘制圆
            canvas.drawOval(
                bounds,
                paint
            )
            paint.xfermode = proterDuffXfermode
            canvas.drawBitmap(it, startX, startY, paint)
            paint.xfermode = null
            canvas.restoreToCount(count)
        }

    }
}