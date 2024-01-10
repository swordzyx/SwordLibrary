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
import sword.logger.SwordLog
import sword.dp

/**
 * 多点触控 - 接力型
 */
class MultiTouchView2(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
  private val tag = "MultiTouchView2【接力型】"
  private val imageWidth = 200f.dp
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val bitmap = BitmapUtil.createBitmap(resources, R.drawable.avatar_rengwuxian, imageWidth, imageWidth)
  
  private var originalOffsetX = 0f
  private var originalOffsetY = 0f

  private var offsetX = 0f
  private var offsetY = 0f
  
  private var downX = 0f
  private var downY = 0f
  private var currentPointerId = 0

  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
        originalOffsetX = offsetX
        originalOffsetY = offsetY
        //按下事件保存按下的手指作为跟踪手指
        var pointerIndex = event.actionIndex
        
        for (i in 0 until event.pointerCount) {
          SwordLog.debug(tag, "point $i , id: ${event.getPointerId(i)}")
        }
        
        //非最后一根手指抬起，index 会发生变化，获取到需要跟踪的手指的 index。
        //如果抬起的就是当前正在滑动的手指，则自动将上一根按下的手指作为跟踪手指（event.actionIndex - 1）
        //如果抬起地手指不是正在滑动地手指，则获取当前跟踪的手指对应的 index
        if (event.actionMasked == MotionEvent.ACTION_POINTER_UP) {
          pointerIndex = if (event.getPointerId(event.actionIndex) == currentPointerId) {
            event.actionIndex - 1
          } else {
            event.findPointerIndex(currentPointerId)
          }
          
          SwordLog.debug(tag, "up, action index: ${event.actionIndex}, action id: ${event.getPointerId(event.actionIndex)}, currentPointerId: " +
              "$currentPointerId, currentIndex: $pointerIndex")
        }
        
        currentPointerId = event.getPointerId(pointerIndex)
        downX = event.getX(pointerIndex)
        downY = event.getY(pointerIndex)
        
      }
      
      MotionEvent.ACTION_MOVE -> {
        SwordLog.debug(tag, "move currentPointerId: $currentPointerId")
        val pointerIndex = event.findPointerIndex(currentPointerId)
        SwordLog.debug(tag, "move current point Index: $pointerIndex, action index: ${event.actionIndex}, actionId: ${event.getPointerId(event.actionIndex)}")
        val currentX = event.getX(pointerIndex)
        val currentY = event.getY(pointerIndex)
        offsetX = currentX - downX + originalOffsetX
        offsetY = currentY - downY + originalOffsetY
        invalidate()
      }
    }
    return true
  }

  override fun onDraw(canvas: Canvas) {
    
    canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
  }
  
  
  
}