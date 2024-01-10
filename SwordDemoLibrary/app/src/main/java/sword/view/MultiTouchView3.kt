package sword.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.swordlibrary.R
import sword.BitmapUtil
import sword.dp

/**
 * 协作型滑动
 */
class MultiTouchView3(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
    private val tag = "MultiTouchView3"
    private val imageWidth = 200f.dp
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = BitmapUtil.createBitmap(resources, R.drawable.avatar_rengwuxian, imageWidth, imageWidth)

    private var originalOffsetX = 0f
    private var originalOffsetY = 0f

    private var offsetX = 0f
    private var offsetY = 0f

    private var downX = 0f
    private var downY = 0f
    private var currentPointId = 0
    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var sumX = 0f
        var sumY = 0f

        var pointerCount = event.pointerCount
        val isPointUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP
        for (i in 0 until event.pointerCount) {
            if(!(isPointUp && i == event.actionIndex)) {
                sumX += event.getX(i)
                sumY += event.getY(i)
            }
        }

        if (isPointUp) {
            --pointerCount
        }

        val focusX = sumX / pointerCount
        val focusY = sumY / pointerCount

        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                downX = focusX
                downY = focusY
                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = focusX - downX + originalOffsetX
                offsetY = focusY - downY + originalOffsetY
                invalidate()
            }
        }
        return true
    }
}