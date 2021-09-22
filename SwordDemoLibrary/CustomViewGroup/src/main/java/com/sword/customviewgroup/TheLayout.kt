package com.sword.customviewgroup

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TheLayout(context: Context): ViewGroup(context) {
    val header = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        setImageResource(R.drawable.header)
        layoutParams = LayoutParams(MATCH_PARENT, 280.dp)
        addView(this)
    }

    val fab = FloatingActionButton(context).apply {
        setImageResource(R.drawable.ic_baseline_swap_horiz_24)
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
            it.marginEnd = 12.dp
        }
        addView(this)
    }

    val avatar = AppCompatImageView(context).apply {

    }

    val itemTitle = AppCompatTextView(context).apply {

    }

    val itemMessage = AppCompatTextView(context).apply {

    }

    val reply = AppCompatImageView(context).apply {

    }


    /**
     * 布局
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        header.let {
            it.layout(0, 0, it.measuredWidth, it.measuredHeight)
        }
    }

    /**
     * 测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        header.autoMeasure()
        fab.autoMeasure()
        avatar.autoMeasure()
        reply.autoMeasure()

        val itemTextWidth = (measuredWidth
                - avatar.measuredWidthWithMargins
                - reply.measuredWidthWithMargins
                - itemTitle.marginLeft
                - itemTitle.marginRight)

        itemTitle.measure(itemTextWidth.toExactlyMeasureSpec(), itemTitle.defaultHeightMeasureSpec(this))
        itemMessage.measure(itemTextWidth.toExactlyMeasureSpec(), itemMessage.defaultHeightMeasureSpec(this))

        val max = (avatar.marginTop + itemTitle.measuredHeightWithMargins + itemMessage.measuredHeightWithMargins).coerceAtLeast(avatar.measuredHeightWithMargins)
        val wrapContentHeight = header.measuredHeightWithMargins + max
        setMeasuredDimension(measuredWidth, wrapContentHeight)
    }

    private val Int.dp
        get() = (this * resources.displayMetrics.density + 0.5f).toInt()

    private fun View.layout(x: Int, y: Int, fromRight: Boolean = false) {
        if (!fromRight) {
            layout(x, y, x + measuredWidth, y + measuredHeight)
        } else {
            layout(this@TheLayout.measuredWidth - x - measuredWidth, y)
        }
    }

    private fun View.autoMeasure() {
        measure(
            this.defaultWithMeasureSpec(this@TheLayout),
            this.defaultHeightMeasureSpec(this@TheLayout)
        )
    }

    /**
     * 默认宽度测量
     */
    private fun View.defaultWithMeasureSpec(parentView: ViewGroup): Int {
        return when(layoutParams.width) {
            MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
            WRAP_CONTENT -> WRAP_CONTENT.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.width.toExactlyMeasureSpec()
        }
    }

    /**
     * 默认高度测量
     */
    private fun View.defaultHeightMeasureSpec(parentView: ViewGroup): Int {
        return when(layoutParams.height) {
            MATCH_PARENT -> parentView.measuredHeight.toExactlyMeasureSpec()
            WRAP_CONTENT -> WRAP_CONTENT.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.height.toExactlyMeasureSpec()
        }
    }

    private fun Int.toExactlyMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)
    }

    private fun Int.toAtMostMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)
    }

}