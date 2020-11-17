package com.sword.customviewset.touchdrag

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children


const val COL = 2
const val ROW = 3
class DragListenerGridView(context: Context, attrs: AttributeSet): ViewGroup(context, attrs) {

    private var dragListener: OnDragListener = CustomDragListener()
    private var orderedChild: MutableList<View> = ArrayList()
    private var dragedView: View? = null

    init {
        isChildrenDrawingOrderEnabled = true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (child in children) {
            orderedChild.add(child)
            child.setOnLongClickListener {v ->
                dragedView = v
                v.startDrag(null, DragShadowBuilder(v), v, 0)
                false
            }
            child.setOnDragListener(dragListener)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)
        val childWidth = specWidth / COL
        val childHeight = specHeight / ROW
        measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
        setMeasuredDimension(specWidth, specHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childWidth = width / COL
        val childHeight = height / ROW
        var left: Int
        var top: Int
        for ((index, child) in children.withIndex()) {
            left = index % 2 * childWidth
            top = index / 2 * childHeight
            child.layout(0, 0, childWidth, childHeight)
            child.translationX = left.toFloat()
            child.translationY = top.toFloat()
        }
    }

    inner class CustomDragListener: OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when(event.action) {
                DragEvent.ACTION_DRAG_STARTED -> if (event.localState === v) {
                    v.visibility = View.INVISIBLE
                }
                DragEvent.ACTION_DRAG_ENTERED -> if (event.localState !== v) {
                    sort1(v)
                }
                DragEvent.ACTION_DRAG_EXITED -> {}
                DragEvent.ACTION_DRAG_ENDED -> if(event.localState === v) {
                    v.visibility = View.VISIBLE
                }
            }
            return true
        }

        /*private fun sort(v: View) {
            originalLeft = v.left
            originalTop = v.top
            val transX = targetLeft - originalLeft
            val transY = targetTop - originalTop
            targetLeft = originalLeft
            targetTop = originalTop

            v.animate().translationX(transX.toFloat()).translationY(transY.toFloat())
        }*/

        fun sort1(v: View) {
            var targetIndex = -1
            var dragedIndex = -1

            for ((index, child) in orderedChild.withIndex()) {
                if (v === child) {
                    targetIndex = index
                } else if (dragedView === child) {
                    dragedIndex = index
                }
            }
            orderedChild.removeAt(dragedIndex)
            orderedChild.add(targetIndex, dragedView!!)
            var childLeft : Int
            var childTop : Int
            val childWidth = width / COL
            val childHeight = height / ROW
            for ((index, child) in orderedChild.withIndex()) {
                childLeft = index % 2 * childWidth
                childTop = index / 2 * childHeight
                child.animate()
                        .translationX(childLeft.toFloat())
                        .translationY(childTop.toFloat())
                        .setDuration(150)
            }
        }

        private fun sort(targetView: View) {
            var draggedIndex = -1
            var targetIndex = -1
            for ((index, child) in orderedChild.withIndex()) {
                if (targetView === child) {
                    targetIndex = index
                } else if (dragedView === child) {
                    draggedIndex = index
                }
            }
            orderedChild.removeAt(draggedIndex)
            orderedChild.add(targetIndex, dragedView!!)
            var childLeft: Int
            var childTop: Int
            val childWidth = width / COL
            val childHeight = height / ROW
            for ((index, child) in orderedChild.withIndex()) {
                childLeft = index % 2 * childWidth
                childTop = index / 2 * childHeight
                child.animate()
                        .translationX(childLeft.toFloat())
                        .translationY(childTop.toFloat())
                        .setDuration(150)
            }
        }
    }


}