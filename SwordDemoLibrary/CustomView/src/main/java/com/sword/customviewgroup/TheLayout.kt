package com.sword.customviewgroup

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TheLayout(context: Context): CustomLayout(context) {
    val header = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        setImageResource(R.drawable.header)
        layoutParams = LayoutParams(MATCH_PARENT, 280.dp)
        addView(this)
    }

    val fab = FloatingActionButton(context).apply {
        setImageResource(R.drawable.ic_baseline_swap_horiz_24)
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
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
        header.layout(0,0)
        fab.let { it.layout(it.marginRight, header.bottom - (it.measuredHeight / 2), true) }
        avatar.let { it.layout(it.marginLeft, header.bottom + marginTop) }
        itemTitle.let { it.layout(avatar.right + it.marginLeft, avatar.top + it.marginTop)}
        itemMessage.let { it.layout(avatar.right + it.marginLeft, itemTitle.bottom + it.marginTop) }
        reply.let { it.layout(it.marginRight, avatar.top + it.marginTop, true)}
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
}