package sword.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.children
import sword.SwordLog
import kotlin.math.abs

/**
 * 自定义 View 触摸反馈：ViewGroup 的触摸反馈 作业
 */
class TwoPager(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {
  private val tag = "TwoPager"
  private var downScrollX = 0
  private var downX = 0f
  private var downY = 0f

  private val viewConfiguration = ViewConfiguration.get(context)
  private val velocityTracker = VelocityTracker.obtain()
  private val overScroll = OverScroller(context)

  /**
   * 测量
   */
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    measureChildren(widthMeasureSpec, heightMeasureSpec)
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
  }

  /**
   * 布局
   */
  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    var childLeft = 0
    children.forEach { child ->
      child.layout(childLeft, 0, childLeft + child.measuredWidth, child.measuredHeight)
      childLeft += measuredWidth
    }
  }

  /**
   * 拦截
   */
  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    var result = false
    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
      velocityTracker.clear()
    }
    velocityTracker.addMovement(event)

    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        downX = event.x
        downY = event.y
        downScrollX = scrollX
      }
      
      MotionEvent.ACTION_MOVE -> {
        val offsetX = event.x - downX
        if (abs(offsetX) > viewConfiguration.scaledPagingTouchSlop) {
          result = true
          parent.requestDisallowInterceptTouchEvent(true)
        }
      }
    }
    return result
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
      velocityTracker.clear()
    }
    velocityTracker.addMovement(event)

    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        downX = event.x
        downY = event.y
        downScrollX = scrollX
      }
      MotionEvent.ACTION_MOVE -> {
        val dx = (downX - event.x + downScrollX).toInt()
          .coerceAtLeast(0)
          .coerceAtMost(width)
        scrollTo(dx, 0)
      }
      MotionEvent.ACTION_UP -> {
        velocityTracker.computeCurrentVelocity(
          1000,
          viewConfiguration.scaledMaximumFlingVelocity.toFloat()
        )
        //往左为负，往右为正
        val vx = velocityTracker.xVelocity
        SwordLog.debug(tag, "xVelocity: $vx")

        val distanceX = if (abs(vx) < viewConfiguration.scaledMinimumFlingVelocity) {
          if (scrollX > width / 2) {
            width - scrollX
          } else {
            -scrollX
          }
        } else {
          if (vx > 0) {
            -scrollX
          } else {
            width - scrollX
          }
        }
        overScroll.startScroll(scrollX, 0, distanceX, 0)
        postInvalidateOnAnimation()
      }
    }
    return true
  }

  override fun computeScroll() {
    if (overScroll.computeScrollOffset()) {
      SwordLog.debug(tag, "computeScroll, currX: ${overScroll.currX}, currY: ${overScroll.currY}")
      scrollTo(overScroll.currX, overScroll.currY)
      postInvalidateOnAnimation()
    }
  }
}