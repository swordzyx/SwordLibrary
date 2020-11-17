package com.sword.customviewset.touchdrag

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import java.util.*

private const val COLUMNS = 2
private const val rows = 3

class HenCoderDragListenerView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private var dragListener: OnDragListener = HenDragListener()
    private var draggedView: View? = null
    private var orderedChildren: MutableList<View> = ArrayList()

    init {
        isChildrenDrawingOrderEnabled = true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (child in children) {
            orderedChildren.add(child) // 初始化位置
            child.setOnLongClickListener { v ->
                draggedView = v
                //Android 提供了两个方法用于拖拽，一个是 startDrag ，一个是 startDragAndDrop ，
                //第三个参数为本地参数，这是随时都可以获取到的，它是最开始执行拖拽操作的 View，
                //第一个参数在松手的时候才能拿到。也就是收到 DRAG_DROP 事件时。第一个参数可以跨进程
                //startDrag 的主要应用场景是对数据的跨进程拖拽，是一个比较重量级的操作
                v.startDrag(null, DragShadowBuilder(v), v, 0)
                false
            }
            //给每一个子 view 设置监听器，当一个子 view 被拖动时，其他子 view 设置的监听器也会被回调
            child.setOnDragListener(dragListener)
        }
    }

    //重写 onDragEvent 可以实现与设置 OnDragListener 同样的效果，即监听拖拽事件。
    //不管是否设置了 OnDragListener ， onDragEvent 都会被回调
    override fun onDragEvent(event: DragEvent?): Boolean {
        return super.onDragEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)
        val childWidth = specWidth / COLUMNS
        val childHeight = specHeight / rows
        measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
        setMeasuredDimension(specWidth, specHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft: Int
        var childTop: Int
        val childWidth = width / COLUMNS
        val childHeight = height / rows
        for ((index, child) in children.withIndex()) {
            childLeft = index % 2 * childWidth
            childTop = index / 2 * childHeight
            child.layout(0, 0, childWidth, childHeight)
            child.translationX = childLeft.toFloat()
            child.translationY = childTop.toFloat()
        }
    }

    private inner class HenDragListener : OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            //event.localState === v 是判断触发事件的 View 是不是最开始执行操作的 View
            //event.localState 指示的是最开始执行拖拽操作的 view
            //v 指示的是当前触发事件的 View
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> if (event.localState === v) {
                    v.visibility = View.INVISIBLE
                }
                DragEvent.ACTION_DRAG_ENTERED -> if (event.localState !== v) {
                    sort(v)
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                }
                DragEvent.ACTION_DRAG_ENDED -> if (event.localState === v) {
                    v.visibility = View.VISIBLE
                }
            }
            return true
        }
    }

    private fun sort(targetView: View) {
        var draggedIndex = -1
        var targetIndex = -1
        for ((index, child) in orderedChildren.withIndex()) {
            if (targetView === child) {
                targetIndex = index
            } else if (draggedView === child) {
                draggedIndex = index
            }
        }
        orderedChildren.removeAt(draggedIndex)
        orderedChildren.add(targetIndex, draggedView!!)
        var childLeft: Int
        var childTop: Int
        val childWidth = width / COLUMNS
        val childHeight = height / rows
        for ((index, child) in orderedChildren.withIndex()) {
            childLeft = index % 2 * childWidth
            childTop = index / 2 * childHeight
            child.animate()
                    .translationX(childLeft.toFloat())
                    .translationY(childTop.toFloat())
                    .setDuration(150)
        }
    }
}
