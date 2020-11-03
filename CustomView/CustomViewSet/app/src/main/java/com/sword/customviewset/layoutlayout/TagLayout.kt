package com.sword.customviewset.layoutlayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max

class TagLayout(context: Context, attrs: AttributeSet): ViewGroup(context, attrs) {

    private var childBoundsList = mutableListOf<Rect>()

    //widthMeasureSpec 和 heightMeasureSpec 是 Layout 的父 View 所提供的宽高值
    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        var usedWidth = 0
        var usedHeight = 0
        var lineUsedHeigh = 0
        var lineUsedWidth = 0
        //获取 Layout 的期望，根据开发者所设定的宽高和父 View 限制的宽高条件
        for ((index, child) in children.withIndex()) {
            //测量子 View 的宽高，依赖于 Layout 的宽高，Layout 的宽高依赖于父 Layout 的期望宽高以及开发者所设置的宽高值
//            measureChild(child, widthMeasureSpec, heightMeasureSpec, 0, usedHeight)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, usedHeight)

            //保存所有子 View 的宽高，用于之后的布局。
            if ((lineUsedWidth + child.measuredWidth) > specWidthSize) {
                usedWidth = max(usedWidth, lineUsedWidth)
                usedHeight += lineUsedHeigh
                lineUsedHeigh = 0
                lineUsedWidth = 0
//                measureChild(child, widthMeasureSpec, heightMeasureSpec, 0, usedHeight)
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, usedHeight)
            }


            if (index >= childBoundsList.size) {
                childBoundsList.add(Rect())
            }

            val rect = childBoundsList[index]
            rect.set(lineUsedWidth, usedHeight, lineUsedWidth + child.measuredWidth, usedHeight + child.measuredHeight)
            lineUsedWidth += child.measuredWidth
            lineUsedHeigh = max(lineUsedHeigh, child.measuredHeight)
        }

        //保存测量得到实际宽高
        setMeasuredDimension(usedWidth, usedHeight + lineUsedHeigh)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for ((index,child) in children.withIndex()){
            val bounds = childBoundsList[index]
            child.layout(bounds.left, bounds.top, bounds.right, bounds.bottom)
        }
    }

    private fun measureChild(child: View, measureWidthSpec: Int, measureHeightSpec: Int, usedWidth: Int, usedHeight: Int) {
        //测量子 View 的宽高
        Log.d("SWORD", "measure width")
        val measureChildWidthSpec = computeSpec(layoutParams.width, measureWidthSpec, usedWidth)
        Log.d("SWORD", "measure height")
        val measureChildHeightSpec = computeSpec(layoutParams.height, measureHeightSpec, usedHeight)
        child.measure(measureChildWidthSpec, measureChildHeightSpec)
    }

    private fun computeSpec(layoutParams: Int, spec: Int, used: Int): Int{

        var specChildMode = -1
        var specChildSize = -1
        val measureSpecSize = MeasureSpec.getSize(spec)
        val measureSpecMode = MeasureSpec.getMode(spec)
        var modeString = ""
        var layoutString = ""

        when(layoutParams) {
            LayoutParams.MATCH_PARENT -> {
                layoutString = "MATCH_PARENT"
                when(measureSpecMode){
                    //在视频中，这里的 specChildMode 使用的是 EXACTLY，但是现在的代码中如果使用 EXACTLY，就会出现第一个子 View 就占满全屏的情况，原因是第一个子 view 的宽高就是全屏了。还没有找到解决方案。
                    MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> {
                        modeString = "AT_MOST or EXACTLY"
                        specChildMode = MeasureSpec.AT_MOST
                        specChildSize = measureSpecSize - used
                    }
                    MeasureSpec.UNSPECIFIED -> {
                        modeString = "UNSPECIFIED"
                        specChildMode = MeasureSpec.UNSPECIFIED
                        specChildSize = 0
                    }
                }
            }
            LayoutParams.WRAP_CONTENT -> {
                layoutString = "WRAP_CONTENT"
                when(MeasureSpec.getMode(spec)){
                    MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> {
                        modeString = "AT_MOST or EXACTLY"
                        specChildMode = MeasureSpec.AT_MOST
                        specChildSize = measureSpecSize - used
                    }
                    MeasureSpec.UNSPECIFIED -> {
                        modeString = "UNSPECIFIED"
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
        Log.d("SWORD", "layout param: $layoutString ; layout mode: $modeString; child size: $specChildSize")
        return MeasureSpec.makeMeasureSpec(specChildSize, specChildMode)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

}