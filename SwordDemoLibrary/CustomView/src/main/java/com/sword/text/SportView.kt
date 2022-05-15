package com.sword.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.sword.LogUtil
import com.sword.dp
import com.sword.customViewDrawing.dp

private val CIRCLE_COLOR = Color.parseColor("#90A4AE")
private val HIGHLIGHT_COLOR = Color.parseColor("#FF4081")
private val RADIUS = 150.dp.toFloat()
private val RING_WIDTH = 20.dp.toFloat()

class SportView(context: Context, attrs: AttributeSet) : View(context, attrs) {
	private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private val startAngle = -90f
	private val sweepAngle = 225f

	private val titleTextSize = 100.dp.toFloat()
	private val titleText = "abab"
	private val textBoundsRect = Rect()
	private val fontMetric = Paint.FontMetrics()

	override fun onDraw(canvas: Canvas) {
		//绘制环
		paint.style = Paint.Style.STROKE
		paint.strokeWidth = RING_WIDTH
		paint.color = CIRCLE_COLOR
		canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)

		//绘制进度条
		paint.strokeCap = Paint.Cap.ROUND
		paint.color = HIGHLIGHT_COLOR
		canvas.drawArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS, startAngle, sweepAngle, false, paint)

		//绘制文字, 正常绘制
		paint.textSize = titleTextSize
		paint.style = Paint.Style.FILL
		canvas.drawText(titleText, width / 2f, height / 2f, paint)

		//-------------------------------- 文字垂直居中
		//绘制文字，通过 getTextBounds 获取文字实际边界距离起始点的偏移，实现纵向居中。
		paint.getTextBounds(titleText, 0, titleText.length, textBoundsRect)
		com.sword.LogUtil.debug("CustomView:SportView", "getTextBounds: ${textBoundsRect.left}, ${textBoundsRect.top}, ${textBoundsRect.right}, ${textBoundsRect.bottom}")
		canvas.drawText(titleText, width / 2f, height / 2f + (textBoundsRect.top + textBoundsRect.bottom) / 2, paint)

		//绘制文字，通过 FontMetric 获取文字的上边界和下边接实现纵向居中
		paint.getFontMetrics(fontMetric)
		//fontMetric.ascent 和 fontMetric.descent 分别表示文字核心部分的顶部和底部，这是跟字体相关，而跟具体某个字无关
		//fontMetric.top 和 fontMetric.bottom 则表示文字最大能达到的上边界和下边界距离文字绘制起始点的距离，一般文字实际显示的边界会离 top 和 bottom 有一定的距离
		com.sword.LogUtil.debug("CustomView:SportView", "getFontMetrics, ascent: ${fontMetric.ascent}; top: ${fontMetric.top}; descent: ${fontMetric.descent}; bottom: ${fontMetric.bottom}; leading: ${fontMetric.leading}")
		canvas.drawText(titleText, width / 2f, height / 2f + (fontMetric.ascent + fontMetric.descent) / 2f, paint)

		//-------------------------------- 文字顶部贴边
		paint.textAlign = Paint.Align.LEFT
		paint.textSize = 150f.dp
		paint.getTextBounds(titleText, 0, titleText.length, textBoundsRect)
		//使用 textBound.top 实现顶部贴边比较适合展示型文字（例如标题等），textBound.top 是在起始点的上方，因此它本身是一个负值，所以需要在 textBoundsRect 前面加上一个负号
		//将文字往左偏移 textBoundsRect.left 是为了去除文字左侧的间隙，默认情况下文字左侧是会有间隙的，这个间隙的距离就是 textBounds.left，它表示了文字实际左侧距离绘制起始点的偏移量。
		canvas.drawText(titleText, -textBoundsRect.left.toFloat(), -textBoundsRect.top.toFloat(), paint)

		//通过将文字下移 fontMetrics.top 的距离实现顶部贴边比较适合阅读型文字，因为文字内容距离边界会有一定的空隙
		paint.textSize = 15f.dp
		paint.getFontMetrics(fontMetric)
		paint.getTextBounds(titleText, 0, titleText.length, textBoundsRect)
		//如果是展示动态文字的话，是不是就没法去除文字左边默认的间隙了。
		canvas.drawText(titleText, -textBoundsRect.left.toFloat(), -fontMetric.top, paint)
	}
}