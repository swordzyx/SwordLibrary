package com.example.swordlibrary.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.sword.LogUtil

class DragListenerGridView(context: Context, attrs: AttributeSet? = null) :
  ViewGroup(context, attrs) {
  private val tag = "DragListenerGridView"
  private val column = 2
  private val row = 3
  private val views = mutableListOf<View>()

  //这里返回 false，View.onDragEvent 就会被调用
  private val dragListener = OnDragListener { v, event ->
    when (event.action) {
      DragEvent.ACTION_DRAG_STARTED -> {
        //某个 View 开始被拖拽
        LogUtil.debug(tag, "view#${v.id} 开始被拖拽，" +
            "localState：${(event.localState as View).id}，" +
            "v==localState: ${v == event.localState}")
        if (v == event.localState && v.visibility != View.INVISIBLE) {
          v.visibility = View.INVISIBLE
        }
      }
      DragEvent.ACTION_DRAG_ENTERED -> {
        LogUtil.debug(tag, "进入 view#${v.id}，" +
            "localState：${(event.localState as View).id}，" +
            "v==localState: ${v == event.localState}")
        //手指进入某个 View 的区域内
        if (v == event.localState) {
          return@OnDragListener true
        }
        
        val originIndex = views.indexOf(event.localState as View)
        val aimIndex = views.indexOf(v)
        if (aimIndex > originIndex) {
          for (i in originIndex until aimIndex) {
            views[i] = views[i + 1]
          }
        } else {
          for (i in originIndex - 1 downTo aimIndex) {
            views[i + 1] = views[i]
          }
        }
        views[aimIndex] = event.localState as View
        requestLayout()
      }
      DragEvent.ACTION_DRAG_LOCATION -> {
        LogUtil.debug(tag, "当前所在 view#${v.id}，x: ${event.x}, y: ${event.y}" +
            "localState：${(event.localState as View).id}，" +
            "v==localState: ${v == event.localState}")
      }
      DragEvent.ACTION_DRAG_EXITED -> {
        LogUtil.debug(tag, "退出 view#${v.id}，" +
            "localState：${(event.localState as View).id}，" +
            "v==localState: ${v == event.localState}")
      }
      DragEvent.ACTION_DRAG_ENDED -> {
        LogUtil.debug(tag, "拖拽结束 view#${v.id}，" +
            "localState：${(event.localState as View).id}，" +
            "v==localState: ${v == event.localState}，getResult: ${event.result}")
      }
      DragEvent.ACTION_DROP -> {
        LogUtil.debug(tag, "拖拽取消 view#${v.id}，" +
            "localState：${(event.localState as View).id}，" +
            "v==localState: ${v == event.localState}")
        if (v.visibility != View.VISIBLE) {
          v.visibility = View.VISIBLE
        }
      }
    }
    true
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    LogUtil.debug(tag, "onFinishInflate, 子 View 数量：$childCount")
    children.forEach { child ->
      views.add(child)
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
    LogUtil.debug(tag, "onFinishInflate, views 数量：${views.size}")
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = MeasureSpec.getSize(widthMeasureSpec)
    val widthSpec = MeasureSpec.makeMeasureSpec(
       width / column,
      MeasureSpec.EXACTLY
    )

    val height = MeasureSpec.getSize(heightMeasureSpec)
    val heightSpec = MeasureSpec.makeMeasureSpec(
       height / row,
      MeasureSpec.EXACTLY
    )

    children.forEach { child ->
      child.measure(widthSpec, heightSpec)
    }
    setMeasuredDimension(width, height)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val columnWidth = measuredWidth / column
    val rowHeight = measuredHeight / row
    views.forEachIndexed { index, child ->
      val rowIndex = index / column
      val columnIndex = index % column
      val left = columnIndex * columnWidth
      val top = rowIndex * rowHeight
      LogUtil.debug(tag, "$rowIndex 行 $columnIndex 列，左：$left，上：$top， index：$index")
      child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
    }
  }
}