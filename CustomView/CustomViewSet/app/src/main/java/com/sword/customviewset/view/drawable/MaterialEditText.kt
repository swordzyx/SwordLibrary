package com.sword.customviewset.view.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.sword.customviewset.view.dp

private val TEXT_SIZE = 12.dp
private val TEXT_MARGIN = 3.dp
private val VERTICAL_OFFSET = TEXT_MARGIN + TEXT_SIZE + 3.dp

class MaterialEditText(context: Context, attrs: AttributeSet): AppCompatEditText(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
    }
    private var bound = Rect()

    init {
        setPadding(paddingLeft, (paddingTop + TEXT_SIZE + TEXT_MARGIN).toInt(), paddingRight, paddingBottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.getTextBounds(hint.toString(), 0, hint.length, bound)
        canvas.drawText(hint.toString(), paddingLeft.toFloat(), VERTICAL_OFFSET, paint)
    }
}
