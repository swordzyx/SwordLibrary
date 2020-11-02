package com.sword.customviewset.layoutlayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.sword.customviewset.view.dp
import java.util.*

private val COLORS = intArrayOf(
    Color.parseColor("#E91E63"),
    Color.parseColor("#673AB7"),
    Color.parseColor("#3F51B5"),
    Color.parseColor("#2196F3"),
    Color.parseColor("#009688"),
    Color.parseColor("#FF9800"),
    Color.parseColor("#FF5722"),
    Color.parseColor("#795548")
)

private val TEXT_SIZES = intArrayOf(16, 22, 28)
private val CORNER_RADIUS = 4.dp
private val X_PADDING = 16.dp.toInt()
private val Y_PADDING = 8.dp.toInt()

class ColoredTextView(context: Context, attrs: AttributeSet): AppCompatTextView(context, attrs) {
    private val random = Random()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = random.nextInt(TEXT_SIZES.size).toFloat()
        color = COLORS[random.nextInt(COLORS.size)]
    }

    init {
        setPadding(X_PADDING, Y_PADDING, X_PADDING, Y_PADDING)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), CORNER_RADIUS, CORNER_RADIUS, paint)
        super.onDraw(canvas)
    }
}