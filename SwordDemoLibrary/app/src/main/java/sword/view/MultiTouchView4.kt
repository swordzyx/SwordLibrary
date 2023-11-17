package sword.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import sword.dp

/**
 * 各自为战型滑动
 */
class MultiTouchView4(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { 
    style = Paint.Style.STROKE
    color = Color.BLACK
    strokeWidth = 2f.dp
  }
  private val paths = mutableMapOf<Int, Path>()

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when(event.actionMasked) {
      MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_DOWN -> {
        val path = Path()
        paths[event.getPointerId(event.actionIndex)] = path
        path.moveTo(event.getX(event.actionIndex), event.getY(event.actionIndex))
      }
      MotionEvent.ACTION_MOVE -> {
        for (i in 0 until event.pointerCount) {
          val pointerId = event.getPointerId(i)
          paths[pointerId]?.lineTo(event.getX(i), event.getY(i))
        }
      }
      MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
        val pointerId = event.getPointerId(event.actionIndex)
        paths.remove(pointerId)
      }
    }
    invalidate()
    return true
  }

  override fun onDraw(canvas: Canvas) {
    paths.forEach {
      canvas.drawPath(it.value, paint)
    }
  }
  
}