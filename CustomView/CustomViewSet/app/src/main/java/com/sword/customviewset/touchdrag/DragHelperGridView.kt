package com.sword.customviewset.touchdrag

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper

const val COLUMN = 2
const val ROWS = 3

class DragHelperGridView(context: Context, attrs: AttributeSet): ViewGroup(context, attrs){
    private var dragHelper = ViewDragHelper.create(this, DragCallback())

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)

        val childWidth = specWidth/2
        val childHeight = specHeight/2

        measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
        setMeasuredDimension(specWidth, specHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childWidth = width / COLUMN
        val childHeight = height / ROWS
        var left: Int
        var top: Int

        for ((index, child) in children.withIndex()) {
            left = index % 2 * childWidth
            top = index / 2 * childHeight
            child.layout(left, top, left + childWidth, top + childHeight)
        }
    }

    override fun onInterceptHoverEvent(event: MotionEvent): Boolean {
        return dragHelper.shouldInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    inner class DragCallback: ViewDragHelper.Callback() {
        //全局变量似乎必须要初始化，而方法内的局部变量则可以不初始化
        private var capturedLeft = 0f
        private var capturedTop = 0f

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return true
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            capturedChild.elevation += 1

            capturedLeft = capturedChild.left.toFloat()
            capturedTop = capturedChild.top.toFloat()
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            releasedChild.elevation -= 1
            dragHelper.settleCapturedViewAt(capturedLeft.toInt(), capturedTop.toInt())
            postInvalidateOnAnimation()

        }

    }

}