package com.sword.customviewset.multitouchview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sword.customviewset.getAvator
import com.sword.customviewset.view.dp

private val IMAGE_SIZE = 200.dp.toInt()

class MultiTouchView1(context: Context, attrs: AttributeSet) : View(context, attrs){
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val bitmap = getAvator(resources, IMAGE_SIZE)

    var offsetX = 0f
    var offsetY = 0f
    var downX = 0f
    var downY = 0f
    var originalX = 0f
    var originalY = 0f
    var trackingIndexId = 0

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                trackingIndexId = event.getPointerId(0)
                downX = event.x
                downY = event.y
                originalX = offsetX
                originalY = offsetY
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                trackingIndexId = event.getPointerId(index)
                downX = event.getX(index)
                downY = event.getY(index)
                originalX = offsetX
                originalY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                //在 view 上发生的事件是位于同一序列中的，并且是按顺序的。
                //event.x 和 event.y 获取的是 View 上落下的第一根手指的 x 坐标和 y 坐标，对应的 index 为 0
                //当第一根手指抬起时，View 上原本第二个落下的手指的 index 从 1 变为 0，也就是变为了第一根手指。
                val index = event.findPointerIndex(trackingIndexId)
                offsetX = event.getX(index) - downX + originalX
                offsetY = event.getY(index) - downY + originalY
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val index = event.actionIndex
                val id = event.findPointerIndex(index)
                if (id == trackingIndexId) {
                    var newIndex = if(index == (event.pointerCount - 1)) {
                        event.pointerCount - 2
                    } else {
                        event.pointerCount - 1
                    }
                    trackingIndexId = event.getPointerId(newIndex)
                    downX = event.getX(newIndex)
                    downY = event.getY(newIndex)
                    originalX = offsetX
                    originalY = offsetY
                }
            }
        }
        return true

    }
}