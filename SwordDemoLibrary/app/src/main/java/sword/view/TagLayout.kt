package sword.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.*
import sword.logger.SwordLog
import kotlin.math.max

class TagLayout(context: Context, attrs: AttributeSet? = null): ViewGroup(context, attrs) {
    private val tag = "TagLayout"
    private val childBounds = mutableListOf<Rect>()

    init {
        isScrollContainer = true
        isVerticalScrollBarEnabled = true
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

        var widthUsed = 0
        var heightUsed = 0
        var lineMaxWidth = 0
        var lineMaxHeight = 0

        children.forEachIndexed { index, child ->
            SwordLog.debug(tag, "child $index ")
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)

            if ((widthUsed + child.measuredWidth) > maxWidth) {
                widthUsed = 0
                heightUsed += lineMaxHeight
                lineMaxHeight = 0
                measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed)
            }

            var childBound = childBounds.getOrNull(index)
            if (childBound == null) {
                childBound = Rect()
                childBounds.add(childBound)
            }
            childBound.set(
                widthUsed + child.marginLeft,
                heightUsed + child.marginTop,
                widthUsed + child.marginLeft + child.measuredWidth,
                heightUsed + child.marginTop + child.measuredHeight
            )
            widthUsed += child.marginLeft + child.measuredWidth + child.marginRight
            lineMaxHeight = max(child.measuredHeight + child.marginTop + child.marginBottom, lineMaxHeight)
            lineMaxWidth = max(widthUsed, lineMaxWidth)
        }
        SwordLog.debug(tag, "childBound count: ${childBounds.size}")

        setMeasuredDimension(lineMaxWidth, heightUsed + lineMaxHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, child ->
            val bounds = childBounds[index]
            SwordLog.debug(tag, "text: ${(child as ColorTextView).text}, 左：${bounds.left}, 上：${bounds.top}, 右：${bounds.right}, 下：${bounds.bottom}")
            child.layout(bounds.left, bounds.top, bounds.right, bounds.bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}