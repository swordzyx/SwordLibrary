package com.sword.customviewset.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.graphics.toColorInt
import com.sword.customviewset.view.dp

class MeshDrawable : Drawable() {
    private val INTERNAL = 50.dp
    val paint = Paint().apply {
        color = "#F9A825".toColorInt()
        strokeWidth = 5.dp
    }

    override fun draw(canvas: Canvas) {
        //画竖线
        var x = bounds.left.toFloat()
        while (x <= bounds.right.toFloat()) {
            canvas.drawLine(x, bounds.top.toFloat(), x, bounds.bottom.toFloat(), paint)
            x += INTERNAL
        }

        //画横线
        var y = bounds.top.toFloat()
        while (y <= bounds.bottom.toFloat()) {
            canvas.drawLine(bounds.left.toFloat(), y, bounds.right.toFloat(), y, paint)
            y += INTERNAL
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getAlpha(): Int {
        return paint.alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getColorFilter(): ColorFilter {
        return paint.colorFilter
    }

    override fun getOpacity(): Int {
        return when(paint.alpha) {
            1 -> PixelFormat.OPAQUE
            0 -> PixelFormat.TRANSPARENT
            else -> PixelFormat.TRANSLUCENT
        }
    }

}