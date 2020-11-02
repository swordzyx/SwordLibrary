package com.sword.customviewset.layoutlayout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max

class TagLayout(context: Context, attrs: AttributeSet): ViewGroup(context, attrs) {
    var usedWidth = 0
    var usedHeight = 0
    var childBoundsList = mutableListOf<Rect>()

    //widthMeasureSpec 和 heightMeasureSpec 是 Layout 的父 View 所提供的宽高值
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //获取 Layout 的期望，根据开发者所设定的宽高和父 View 限制的宽高条件
        for ((index, child) in children.withIndex()) {
            //测量子 View 的宽高，依赖于 Layout 的宽高，Layout 的宽高依赖于父 Layout 的期望宽高以及开发者所设置的宽高值
            measureChild(child, widthMeasureSpec, heightMeasureSpec, usedWidth, usedHeight)

            if (index >= childBoundsList.size) {
                childBoundsList.add(Rect())
            }
            var rect = childBoundsList[index]
            rect.set(usedWidth, usedHeight, usedWidth + child.measuredWidth, usedHeight + child.measuredHeight)
            usedWidth += child.measuredWidth
            usedHeight = max(usedHeight, child.measuredHeight)
        }


        //保存测量得到实际宽高
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (child in children){
            child.layout(0, 0, r - l, b - t)
        }
    }

    fun measureChild(child: View, measureWidthSpec: Int, measureHeightSpec: Int, usedWidth: Int, usedHeight: Int) {
        //测量子 View 的宽高
        var measureChildWidthSpec = computeSpec(layoutParams.width, measureWidthSpec, usedWidth)
        var measureChildHeightSpec = computeSpec(layoutParams.height, measureHeightSpec, usedHeight)
        child.measure(measureChildWidthSpec, measureChildHeightSpec)
    }

    fun computeSpec(layoutParams: Int, spec: Int, used: Int): Int{

        var specChildMode = -1
        var specChildSize = -1
        var measureSpecSize = MeasureSpec.getSize(spec)
        var measureSpecMode = MeasureSpec.getMode(spec)

        when(layoutParams) {
            LayoutParams.MATCH_PARENT -> {
                when(measureSpecMode){
                    MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> {
                        specChildMode = MeasureSpec.EXACTLY
                        specChildSize = measureSpecSize - used
                    }
                    MeasureSpec.UNSPECIFIED -> {
                        specChildMode = MeasureSpec.UNSPECIFIED
                        specChildSize = 0
                    }
                }
            }
            LayoutParams.WRAP_CONTENT -> {
                when(MeasureSpec.getMode(spec)){
                    MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> {
                        specChildMode = MeasureSpec.AT_MOST
                        specChildSize = measureSpecSize - used
                    }
                    MeasureSpec.UNSPECIFIED -> {
                        specChildMode = MeasureSpec.UNSPECIFIED
                        specChildSize = 0
                    }
                }
            }
            else -> {
                specChildMode = MeasureSpec.EXACTLY
                specChildSize = layoutParams
            }
        }
        return MeasureSpec.makeMeasureSpec(specChildSize, specChildMode)
    }

}