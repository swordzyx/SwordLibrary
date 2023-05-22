package sword.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.example.swordlibrary.R
import com.sword.LogUtil
import com.sword.createBitmap1
import com.sword.dp

/**
 * 单点触摸
 */
class MultiTouchView1(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
  private val tag = "MutlTouchView1-单点触摸"
  private val imageSize = dp(300).toFloat()
  private val bitmap = createBitmap1(resources, R.drawable.avatar_rengwuxian, imageSize, imageSize)
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var offsetX = 0f
  private var offsetY = 0f
  
  private var touchDownX = 0f
  private var touchDownY = 0f
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when(event.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        touchDownX = event.x
        touchDownY = event.y
      }
      MotionEvent.ACTION_MOVE -> {
        val deltaX = event.x - touchDownX
        val deltaY = event.y - touchDownY
        val distances = deltaX * deltaX + deltaY * deltaY
        val viewConfiguration = ViewConfiguration.get(context)
        var slopSquare = (viewConfiguration.scaledTouchSlop * viewConfiguration.scaledTouchSlop).toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          slopSquare *= viewConfiguration.scaledAmbiguousGestureMultiplier * viewConfiguration.scaledAmbiguousGestureMultiplier
        }
        LogUtil.debug(tag, "deltaX: $deltaX, deltaY: $deltaY, distances: $distances")

        if (distances > slopSquare) {
          offsetX = deltaX
          offsetY = deltaY
          invalidate()
        }
      }
    }
    return true
  }
  
}