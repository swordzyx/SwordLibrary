package sword.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper
import sword.SwordLog
import kotlin.math.abs

class DragUpDownLayout(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val tag = "DragUpDownLayout"
    private val viewConfiguration = ViewConfiguration.get(context)

    init {
        orientation = VERTICAL
    }

    private val dragHelperCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return true
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return 0
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            SwordLog.debug(
                tag, "view#${children.indexOf(releasedChild)} 释放，" +
                        "水平方向速度：$xvel，垂直方向速度：$yvel, " +
                        "view 坐标：(${releasedChild.left}, ${releasedChild.top})"
            )
            if (abs(yvel) > viewConfiguration.scaledMinimumFlingVelocity) {
                if (yvel > 0) {
                    viewDragHelper.settleCapturedViewAt(0, 0)
                } else {
                    viewDragHelper.settleCapturedViewAt(0, height - releasedChild.height)
                }
            } else {
                if (releasedChild.top > (height - releasedChild.bottom)) {
                    viewDragHelper.settleCapturedViewAt(0, height - releasedChild.height)
                } else {
                    viewDragHelper.settleCapturedViewAt(0, 0)
                }
            }
            postInvalidateOnAnimation()
        }
    }
    private val viewDragHelper = ViewDragHelper.create(this, dragHelperCallback)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (viewDragHelper.continueSettling(true)) {
            postInvalidateOnAnimation()
        }
    }
}