package com.sword.customViewDrawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorSpace
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/*
饼图
 */
private val COLORS = listOf(Color.parseColor("#C2185B"), Color.parseColor("#00ACC1"), Color.parseColor("#558B2F"), Color.parseColor("#5D4037"))
private val ANGLES = floatArrayOf(60f, 90f, 150f, 60f)
private val RADIUS = 150f.dp
private val SELECT_OFFSET = 20f.dp
class PieView(context: Context, attrs: AttributeSet) : View(context, attrs) {
	private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private val selectIndex = 0

	override fun onDraw(canvas: Canvas) {
		var startAngle = 0f
		for ((index, angle) in ANGLES.withIndex()) {
			paint.color = COLORS[index]
			if (index == selectIndex) {
				canvas.save()
				canvas.translate((SELECT_OFFSET * cos(Math.toRadians((startAngle + angle/2).toDouble()))).toFloat(), SELECT_OFFSET * sin((startAngle + angle).toDouble()).toFloat())
			}
			canvas.drawArc(width / 2 - RADIUS, height / 2 - RADIUS, width / 2 + RADIUS, height / 2 + RADIUS, startAngle, angle, true, paint)
			if(index == selectIndex) {
				canvas.restore()
			}
			startAngle += angle
		}
	}
}
