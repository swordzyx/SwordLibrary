package com.sword.customviewset.touchviewgroup


import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import androidx.core.view.children
import kotlin.math.abs


class TwoPager(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    private var downX = 0f
    private var downY = 0f
    private var downScrollX = 0f
    private var scrolling = false
    private val overScroller: OverScroller = OverScroller(context)
    private val viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)
    private val velocityTracker = VelocityTracker.obtain()
    private var minVelocity = viewConfiguration.scaledMinimumFlingVelocity
    private var maxVelocity = viewConfiguration.scaledMaximumFlingVelocity
    private var pagingSlop = viewConfiguration.scaledPagingTouchSlop

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = 0
        val childTop = 0
        var childRight = width
        val childBottom = height
        for (child in children) {
            child.layout(childLeft, childTop, childRight, childBottom)
            childLeft += width
            childRight += width
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        velocityTracker.addMovement(event)
        var result = false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scrolling = false
                downX = event.x
                downY = event.y
                downScrollX = scrollX.toFloat()
            }
            MotionEvent.ACTION_MOVE -> if (!scrolling) {
                val dx = downX - event.x
                //pagingSlop 开始滑动的阈值，只有当滑动的距离大于该值时，ViewPager 才会认为开始滑动了
                if (abs(dx) > pagingSlop) {
                    scrolling = true
                    //通知父 View 不要拦截
                    parent.requestDisallowInterceptTouchEvent(true)
                    //拦截此事件序列不要往子 View 派发
                    result = true
                }
            }
        }
        return result
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                //按下点的记录以及初始偏移的记录
                downX = event.x
                downY = event.y
                downScrollX = scrollX.toFloat()
            }
            MotionEvent.ACTION_MOVE -> { //手指开始移动，则 View 也开始移动。
                //滑动距离的最小值和最大值
                val dx = (downX - event.x + downScrollX).toInt()
                    .coerceAtLeast(0)
                    .coerceAtMost(width)
                //对 View 所有的内部内容做偏移
                //对应的物理模型是：从子 View 的第几个像素开始显示，例如横向 100，则在屏幕中会显示子 View 从第 100 个像素开始到最后的内容。
                scrollTo(dx, 0)
            }
            MotionEvent.ACTION_UP -> {//实现手指抬起的时候的惯性滑动
                //计算当前的速度，第一个参数为单位，表示 1000ms 之内移动的像素数，第二个参数为速度的上限
                velocityTracker.computeCurrentVelocity(1000, maxVelocity.toFloat()) // 5m/s 5km/h
                //获取 x 轴方向上的速度
                val vx = velocityTracker.xVelocity
                val scrollX = scrollX
                //如果滑动的速度小于最小值，也就是 ViewPager 滑动速度较小的情况，则根据当前滑动的距离来判断显示第 0 页还是第 1 页， minVelocity 由系统提供，最小快划速度
                val targetPage = if (abs(vx) < minVelocity) {
                    //当显示第 1 页的时候，scrollX 是为屏幕宽度 width，当从第 1 页往右滑滑倒第 0 页的时候，scrollX 是逐渐从 width 变为 0 的。
                    //scrollX 可以认为是 View 显示的起始 X 坐标，大于 width/2 表示第 0 页有超过 width/2 的内容没有被被显示，此时倾向于显示第 0 页。
                    if (scrollX > width / 2) 1 else 0
                } else {
                    //如果滑动的速度比较快，则直接根据速度的方向来决定显示哪一页，速度为负，说明是向左划，则显示第 1 页，速度为正说明是向右滑，显示第 0 页。
                    if (vx < 0) 1 else 0
                }
                val scrollDistance = if (targetPage == 1) width - scrollX else -scrollX
                overScroller.startScroll(getScrollX(), 0, scrollDistance, 0)
                //
                postInvalidateOnAnimation()
            }
        }
        return true
    }

    //invalidate() 会导致 computeScroll() 调用
    override fun computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.currX, overScroller.currY)
            postInvalidateOnAnimation()
        }
    }
}
