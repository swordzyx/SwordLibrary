package sword.view.nestedscroll

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import sword.logger.SwordLog

class SwordNestedScrollView(context: Context, attrs: AttributeSet? = null): NestedScrollView(context, attrs) {
    private val tag = "SwordNestedScrollView"
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)
        SwordLog.debug(tag, "调用 NestedScrollingParent 的 onNestedPreScroll, " +
                "target: ${target.contentDescription}-${target.javaClass.canonicalName}, " +
                "dx: $dx, dy: $dy, consumed: $consumed")
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        SwordLog.debug(tag, "调用 NestedScrollingParent2 的 onNestedPreScroll, " +
                "target: ${target.contentDescription}-${target.javaClass.canonicalName}, " +
                "dx: $dx, dy: $dy, consumed: $consumed, type: $type")
        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        SwordLog.debug(tag, "调用 NestedScrollingParent 的 onNestedScroll 方法，" +
                "target：${target.contentDescription}--${target.javaClass.canonicalName}, " +
                "dxComsumed: $dxConsumed, " +
                "dyComsumed: $dyConsumed, " +
                "dxUnconsumed: $dxUnconsumed, " +
                "dyUnconsumed: $dyUnconsumed")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {

        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        SwordLog.debug(tag, "调用 NestedScrollingParent3 的 onNestedScroll 方法，" +
                "target：${target.contentDescription}--${target.javaClass.canonicalName}, " +
                "dxComsumed: $dxConsumed, " +
                "dyComsumed: $dyConsumed, " +
                "dxUnconsumed: $dxUnconsumed, " +
                "dyUnconsumed: $dyUnconsumed, type: $type, consumed: $consumed")
        super.onNestedScroll(
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        SwordLog.debug(tag, "调用 NestedScrollingParent 的 onNestedScrollAccepted 函数， " +
                "child：${child.contentDescription}--${child.javaClass.canonicalName}, " +
                "target: ${target.contentDescription}--${target.javaClass.canonicalName}, " +
                "axes: ${getAxesString(axes)}")
        super.onNestedScrollAccepted(child, target, axes)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        SwordLog.debug(tag, "调用 NestedScrollingParent2 的 onNestedScrollAccepted 函数， " +
                "child：${child.contentDescription}--${child.javaClass.canonicalName}, " +
                "target: ${target.contentDescription}--${target.javaClass.canonicalName}, " +
                "axes: ${getAxesString(axes)}, type: $type")
        super.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val isInterceptTouchEvent = super.onInterceptTouchEvent(ev)
        SwordLog.debug(tag, "onInterceptTouchEvent 是否拦截：$isInterceptTouchEvent")
        return isInterceptTouchEvent
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val result = super.onTouchEvent(ev)
        SwordLog.debug(tag, "onTouchEvent, result: $result")
        return result
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        val result = super.onStartNestedScroll(child, target, axes, type)
        val axesS = when(axes) {
            ViewCompat.SCROLL_AXIS_HORIZONTAL -> "x 轴"
            ViewCompat.SCROLL_AXIS_VERTICAL -> "y 轴"
            else -> "none"
        }
        SwordLog.debug(tag, "调用 NestedScrollingParent2 的 onStartNestedScroll 方法, " +
                "child: ${child.contentDescription}--${child.javaClass.canonicalName}, " +
                "target: ${target.contentDescription}--${target.javaClass.canonicalName}, " +
                "axes: $axesS, type: $type, 结果：$result")
        return result
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        val result = super.onStartNestedScroll(child, target, axes)
        val axesS = when(axes) {
            ViewCompat.SCROLL_AXIS_HORIZONTAL -> "x 轴"
            ViewCompat.SCROLL_AXIS_VERTICAL -> "y 轴"
            else -> "none"
        }
        SwordLog.debug(tag, "调用 NestedScrollingParent 的 onStartNestedScroll 方法, " +
                "child: ${child.contentDescription}--${child.javaClass.canonicalName}, " +
                "target: ${target.contentDescription}--${target.javaClass.canonicalName}, " +
                "axes: $axesS, 结果：$result")
        return result
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        val result = super.onNestedPreFling(target, velocityX, velocityY)
        SwordLog.debug(tag, "onNestedPreFling, 拦截 Fling 事件，" +
                "target: ${target.contentDescription}--${target.javaClass.canonicalName}, " +
                "velocityX: $velocityX, velocityY: $velocityY, 拦截结果：$result")
        return result
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        val result = super.onNestedFling(target, velocityX, velocityY, consumed)
        SwordLog.debug(tag, "onNestedFling，收到 Fling 事件, " +
                "target: ${target.contentDescription}--${target.javaClass.canonicalName}，" +
                "velocityX: ${velocityX}, velocityY: ${velocityY}, 是否消耗：$result")
        return result
    }

    private fun getAxesString(axes: Int): String {
        return when(axes) {
            ViewCompat.SCROLL_AXIS_HORIZONTAL -> "x 轴"
            ViewCompat.SCROLL_AXIS_VERTICAL -> "y 轴"
            else -> "none"
        }
    }
}