package com.sword.customViewDrawing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

private val RADIUS = 150f.dp
private val POINTER_LENGTH = 120f.dp
private val OPEN_ANGLE = 120f.dp
private val DASH_WIDTH = 2f.dp
private val DASH_LENGTH = 10f.dp
private const val DASH_COUNT = 20
/*
仪表盘·      ·
 */
class DashBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
	//属性：刻度数，弧的角度，指针的长度
	private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private val path = Path()
	private val dash = Path()
	private lateinit var pathEffect: PathDashPathEffect
	private val START_ANGLE = 90 + OPEN_ANGLE
	private val mark = 10

	init {
		paint.style = Paint.Style.STROKE
		paint.strokeWidth = 4f.dp
		dash.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		//1. 测量弧的长度
		path.reset()
		path.addArc(width / 2 - RADIUS, height / 2 - RADIUS, width / 2 + RADIUS , height / 2 + RADIUS, 90 + OPEN_ANGLE / 2, 360 - OPEN_ANGLE)

		//测量圆弧的长度
		val pathMeasure = PathMeasure(path, false)
		pathEffect = PathDashPathEffect(dash, pathMeasure.length / DASH_COUNT, 0f, PathDashPathEffect.Style.ROTATE)
	}


	override fun onDraw(canvas: Canvas) {
		//画弧的时候不设置 PathEffect，画刻度的时候设置 PathEffect。
		//1. 画弧 canvas.drawArc()，设置 paint，设置成空心
		canvas.drawPath(path, paint)

		//2. 画刻度，画刻度要搞清楚这个刻度在坐标系里面应该是什么位置，刻度所在的坐标系的原点就代表了画这个刻度的时候绘制点所在的位置，坐标系原点沿圆弧方向的切线就是 X 轴，原点和圆心连起来的线就是 Y 轴。
		paint.pathEffect = pathEffect
		canvas.drawPath(path, paint)
		paint.pathEffect = null

		//3. 画指针，通过三角函数计算指针的角度。
		//x = 指针长度 * Math.cos(Math.toRadius(90 + 弧的开口角度 / 2 + (360 - 开口角度) / 20 * 5))
		//y = 指针长度 * Math.sin(Matin.toRadius(90 + 弧的开口角度 / 2 + (360 - 开口角度) / 20 * 5))
		canvas.drawLine(0f, 0f,
			POINTER_LENGTH * cos(markToRadian(mark)).toFloat(),
			POINTER_LENGTH * sin(markToRadian(mark)).toFloat(),
			paint
		)

	}

	private fun markToRadian(mark: Int) =
		Math.toRadians((START_ANGLE + (360 - OPEN_ANGLE) / DASH_COUNT * mark).toDouble())
}