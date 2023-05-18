package com.example.swordlibrary.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

class DragListenerGridView(context: Context, attrs: AttributeSet? = null) :
  ViewGroup(context, attrs) {
  private val column = 2
  private val row = 3
  private val views = mutableListOf<View>()

  //这里返回 false，View.onDragEvent 就会被调用
  private val dragListener = OnDragListener { v, event ->
    when (event.action) {
      DragEvent.ACTION_DRAG_STARTED -> {
        //某个 View 开始被拖拽
        
      }
      DragEvent.ACTION_DRAG_ENTERED -> {
        //手指进入某个 View 的区域内
        if (v == event.localState) {
          return@OnDragListener true
        }
        
        val originIndex = (event.localState as View).tag as Int
        val aimIndex = v.tag as Int
        if (aimIndex > originIndex) {
          for ()
        }
      }
    }
    true
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    children.forEachIndexed { index, child ->
      child.tag = index
      child.setOnDragListener(dragListener)
      //长按拖拽
      child.setOnLongClickListener { 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          child.startDragAndDrop(null, DragShadowBuilder(child), child, 0)
        } else {
          child.startDrag(null, DragShadowBuilder(child), child, 0)
        }
      }
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val widthSpec = MeasureSpec.makeMeasureSpec(
      MeasureSpec.getSize(widthMeasureSpec) / column,
      MeasureSpec.EXACTLY
    )

    val height = MeasureSpec.makeMeasureSpec(
      MeasureSpec.getSize(heightMeasureSpec) / row,
      MeasureSpec.EXACTLY
    )

    children.forEach { child ->
      child.measure(widthSpec, height)
    }
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val columnWidth = measuredWidth / column
    val rowHeight = measuredHeight / row
    children.forEach { child ->
      val index = (child.tag as Int)
      val rowIndex = index / row
      val columnIndex = index % row
      val left = columnIndex * columnWidth
      val top = rowIndex * rowHeight
      println("$rowIndex 行 $columnIndex 列，左：$left，上：$top， index：$index")
      child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
    }
  }
}