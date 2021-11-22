package com.example.animations_transitions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircleView(context: Context?, attrs: AttributeSet) : View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  var radius: Float = 50.0f
    set(value) {
      field = value
      invalidate()
    }

  var offset: Float = 0.0f
    set(value) {
      field = value
      invalidate()
    }

  init {
    paint.color = Color.parseColor("#00796B")
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.drawCircle(width / 2f, height / 2f, radius, paint)
  }
}