package com.sword.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sword.customViewDrawing.dp


class XfermodeView(context: Context, attrs: AttributeSet) : View(context, attrs) {
	private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private val circleBitmap = Bitmap.createBitmap(150f.dp.toInt(), 150f.dp.toInt(), Bitmap.Config.ARGB_8888)
	private val squareBitmap = Bitmap.createBitmap(150f.dp.toInt(), 150f.dp.toInt(), Bitmap.Config.ARGB_8888)

	init {
		circleBitmap
	}
}