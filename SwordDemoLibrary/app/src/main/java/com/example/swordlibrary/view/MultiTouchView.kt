package com.example.swordlibrary.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.sword.dp2px

/**
 * 各自为战型自定义绘制，扔物线【自定义 View 触摸反馈：多点触控的原理和写法全解析】作业
 */
class MultiTouchView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

	private val paths = SparseArray<Path>()
	private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.STROKE
		strokeWidth = dp2px(4f)
		strokeCap = Paint.Cap.ROUND
		strokeJoin = Paint.Join.ROUND
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		for (i in 0 until paths.size()) {
			canvas.drawPath(paths.get(i), paint)
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onTouchEvent(event: MotionEvent): Boolean {
		when(event.actionMasked) {
			MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_DOWN -> {
				val path = Path()
				val index = event.actionIndex
				path.moveTo(event.getX(index), event.getY(index))
				paths.append(event.getPointerId(index), path)
				invalidate()
			}
			MotionEvent.ACTION_MOVE -> {
				for (i in 0 until paths.size()) {
					val id = event.getPointerId(i)
					paths.get(id).moveTo(event.getX(i), event.getY(i))
				}
				invalidate()
			}
			MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP -> {
				val id = event.getPointerId(event.actionIndex)
				paths.remove(id)
				invalidate()
			}
		}
		return true
	}
}