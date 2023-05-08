package com.example.swordlibrary.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.swordlibrary.R
import com.sword.LogUtil
import com.sword.createBitmap1
import com.sword.dp

/**
 * 多点触控 - 接力型
 */
class MultiTouchView2(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
  private val tag = "MultiTouchView2【接力型】"
  private val imageWidth = dp(200).toFloat()
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val bitmap = createBitmap1(resources, R.drawable.avatar_rengwuxian, imageWidth, imageWidth)
  
  private var originalOffsetX = 0f
  private var originalOffsetY = 0f

  private var offsetX = 0f
  private var offsetY = 0f
  
  private var lastDownX = 0f
  private var lastDownY = 0f
  private var currentPointerId = 0

  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
        originalOffsetX = offsetX
        currentPointerId = event.getPointerId(event.actionIndex)
      }
      
      MotionEvent.ACTION_MOVE -> {
        val pointerIndex = event.findPointerIndex(currentPointerId)
        val currentX = event.getX(pointerIndex)
        val currentY = event.getY(pointerIndex)
        originalOffsetX += (currentX - lastDownX)
        originalOffsetY += (currentY - lastDownY)
        lastDownX = currentX
        lastDownY = currentY
        LogUtil.debug(tag, "move pointerIndex: $pointerIndex， originalOffsetX：$originalOffsetX," +
            " originalOffsetX: $originalOffsetY, currentX: $currentX, currentY: $currentY")
      }
    }
    return true
  }

  override fun onDraw(canvas: Canvas) {
    canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
  }
  
  
  
}