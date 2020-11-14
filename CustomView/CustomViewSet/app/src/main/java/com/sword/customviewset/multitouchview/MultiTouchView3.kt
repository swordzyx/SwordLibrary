package com.sword.customviewset.multitouchview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.sword.customviewset.view.dp

class MultiTouchView3(context: Context, attrs: AttributeSet): View(context, attrs) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paths = SparseArray<Path>()

    init {

        paint.strokeWidth = 4.dp
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        for (i in 0 until paths.size()) {
            canvas.drawPath(paths.valueAt(i), paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                //我的华为手机在三根手指同时按下并上滑会启用分屏，也就是把事件拦截了，这个不知道要如何解决。即使加上下面这句代码，三指同时按下并上滑之后仅仅只会触发 down ，并不会触发 up 和 move。暂时找不到思路，也不想在这上面花时间。
                parent.requestDisallowInterceptTouchEvent(true)

                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    if (paths.size() > 0) {
                        paths.clear()
                    }
                }
                val path = Path()
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                path.moveTo(event.getX(actionIndex), event.getY(actionIndex))
                paths.append(pointerId, path)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until paths.size()) {
                    val pointerId = event.getPointerId(i)
                    val path = paths.get(pointerId)
                    path.lineTo(event.getX(i), event.getY(i))
                }
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP -> {
                paths.remove(event.getPointerId(event.actionIndex))
                invalidate()
            }
        }
        return true
    }

}