package com.sword.customviewgroup

import android.content.Context
import android.view.ContextMenu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

abstract class CustomLayout(context: Context) : ViewGroup(context) {
	protected val View.measuredWidthWithMargins get() = (measuredWidth + marginLeft + marginRight)
	protected val View.measuredHeightWithMargins get() = (measuredHeight + marginTop + marginBottom)

	protected val Int.dp get() = (this * resources.displayMetrics.density + 0.5f).toInt()

	protected fun View.layout(x: Int, y: Int, fromRight: Boolean = false) {
		if (!fromRight) {
			layout(x, y, x + measuredWidth, y + measuredHeight)
		} else {
			layout(this@CustomLayout.measuredWidth - x - measuredWidth, y)
		}
	}

	protected fun View.autoMeasure() {
		measure(
			this.defaultWithMeasureSpec(this@CustomLayout),
			this.defaultHeightMeasureSpec(this@CustomLayout)
		)
	}

	/**
	 * 默认高度测量
	 */
	protected fun View.defaultHeightMeasureSpec(parentView: ViewGroup): Int {
		return when (layoutParams.height) {
			LayoutParams.MATCH_PARENT -> parentView.measuredHeight.toExactlyMeasureSpec()
			LayoutParams.WRAP_CONTENT -> LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
			0 -> throw IllegalAccessException("Need special treatment for $this")
			else -> layoutParams.height.toExactlyMeasureSpec()
		}
	}

	/**
	 * 默认宽度测量
	 * 使用 LayoutParams.WRAP_CONTENT 来构造 MeasureSpec 在这里是没有问题的。不过这不是规范的写法。
	 * 传递 LayoutParams.WRAP_CONTENT 的不会出错的原因，大概是伴随这个 size 一起传递过去的 WRAP_CONTENT 对应的是 AT_MOST 模式，这个模式下，子 View 大多会完全忽略传过去的 size 值。
	 */
	protected fun View.defaultWithMeasureSpec(parentView: ViewGroup): Int {
		return when (layoutParams.width) {
			LayoutParams.MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
			LayoutParams.WRAP_CONTENT -> LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
			0 -> throw IllegalAccessException("Need special treatment for $this")
			else -> layoutParams.width.toExactlyMeasureSpec()
		}
	}

	protected fun Int.toExactlyMeasureSpec(): Int {
		return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)
	}

	protected fun Int.toAtMostMeasureSpec(): Int {
		return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)
	}

}