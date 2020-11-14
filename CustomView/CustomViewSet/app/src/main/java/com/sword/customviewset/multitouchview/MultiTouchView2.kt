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

class MultiTouchView2(context: Context, attrs: AttributeSet) : View(context, attrs){
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val bitmap = getAvator(resources, IMAGE_SIZE)

    var offsetX = 0f
    var offsetY = 0f
    var downX = 0f
    var downY = 0f
    var originalX = 0f
    var originalY = 0f


    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val isActionPointerUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP
        var centerX = 0f
        var centerY = 0f
        var pointerCount = event.pointerCount

        for (i in 0 until pointerCount) {
            if (!(isActionPointerUp && i==event.actionIndex)){
                centerX += event.getX(i)
                centerY += event.getY(i)
            }
        }
        if (isActionPointerUp){
            pointerCount--
        }
        centerX = centerX / pointerCount
        centerY = centerY / pointerCount

        when(event.actionMasked){
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_POINTER_DOWN  -> {
                downX = centerX
                downY = centerY
                originalX = offsetX
                originalY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                //在 view 上发生的事件是位于同一序列中的，并且是按顺序的。
                //event.x 和 event.y 获取的是 View 上落下的第一根手指的 x 坐标和 y 坐标，对应的 index 为 0
                //当第一根手指抬起时，View 上原本第二个落下的手指的 index 从 1 变为 0，也就是变为了第一根手指。

                offsetX = centerX - downX + originalX
                offsetY = centerY - downY + originalY
                invalidate()
            }
        }
        return true

    }
}