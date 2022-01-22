package com.sword.customViewDrawing

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class DashBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
	override fun onDraw(canvas: Canvas?) {

		//1. 画弧 canvas.drawArc()，设置 paint，设置成空心

		//2. 画刻度，画刻度要搞清楚这个刻度在坐标系里面应该是什么位置，刻度所在的坐标系的原点就代表了画这个刻度的时候绘制点所在的位置，坐标系原点沿圆弧方向的切线就是 X 轴，原点和圆心连起来的线就是 Y 轴
	}
}