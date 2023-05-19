package com.example.swordlibrary.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper
import com.sword.LogUtil

class DragHelperGridView(context: Context, attrs: AttributeSet? = null) :
    ViewGroup(context, attrs) {
    private val tag = "DragHelperGridView"
    private val column = 2
    private val row = 3
    
    private var draggedView: View? = null

    private val viewDragHelperCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        //尝试开始拖拽
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            LogUtil.debug(
                tag,
                "tryCaptureView，view 索引：${children.indexOf(child)}, pointerId: $pointerId"
            )
            return true
        }

        //View 开始被拖拽
        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            LogUtil.debug(
                tag,
                "onViewCaptured, view 索引：${children.indexOf(capturedChild)}, pointerId: $activePointerId"
            )
            capturedChild.elevation += 1
            draggedView = capturedChild
        }

        //水平方向上发生偏移
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            LogUtil.debug(
                tag,
                "clampViewPositionHorizontal，view 索引：${children.indexOf(child)}, left: $left, dx: $dx"
            )
            return left
        }

        //垂直方向上发生偏移
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            LogUtil.debug(
                tag,
                "clampViewPositionVertical，view 索引：${children.indexOf(child)}, top: $top, dy: $dy"
            )
            return top
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            LogUtil.debug(
                tag,
                "onViewPositionChanged, view 索引：${children.indexOf(changedView)}, " +
                        "left: $left, top: $top, dx: $dx, dy: $dy"
            )
        }

        override fun onViewDragStateChanged(state: Int) {
            LogUtil.debug(tag, "onViewDragStateChanged, state: $state")
            if (state == ViewDragHelper.STATE_IDLE) {
                draggedView!!.elevation -= 1
            }
        }

        //手指松开
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val point = calculateLocation(children.indexOf(releasedChild), width / column, height / row)
            LogUtil.debug(
                tag, "onViewReleased, " +
                    "被松开的 View 的索引：${children.indexOf(releasedChild)}, xvel: $xvel, yvel: $yvel" + 
                    "，目标 x：${point.x}，目标 y：${point.y}"
            )
            viewDragHelper.settleCapturedViewAt(point.x, point.y)
            postInvalidateOnAnimation()
        }
    }

    private val viewDragHelper = ViewDragHelper.create(this, viewDragHelperCallback)


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }
    
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, child ->
            val childWidth = measuredWidth / column
            val childHeight = measuredHeight / row
            val childPoint = calculateLocation(index, childWidth, childHeight)
            child.layout(
                childPoint.x,
                childPoint.y,
                childPoint.x + child.measuredWidth,
                childPoint.y + child.measuredHeight
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpec = MeasureSpec.makeMeasureSpec(
            width / column,
            MeasureSpec.EXACTLY
        )

        val height = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpec = MeasureSpec.makeMeasureSpec(
            height / row,
            MeasureSpec.EXACTLY
        )

        children.forEach { child ->
            child.measure(widthSpec, heightSpec)
        }
        setMeasuredDimension(width, height)
    }

    private fun calculateLocation(
        index: Int,
        columnWidth: Int,
        rowHeight: Int
    ): Point {
        val rowIndex = index / column
        val columnIndex = index % column

        val left = columnIndex * columnWidth
        val top = rowIndex * rowHeight
        LogUtil.debug(tag, "$rowIndex 行 $columnIndex 列，左：$left，上：$top， index：$index")
        return Point(left, top)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (viewDragHelper.continueSettling(true)) {
            postInvalidateOnAnimation()
        }
    }
}