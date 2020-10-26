package com.sword.customviewset.view.drawtext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sword.customviewset.R
import com.sword.customviewset.view.dp

class MultilineTextView(context: Context, attrs: AttributeSet): View(context, attrs) {
    val text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."

    val paint = Paint().apply {
        textSize = 50.dp
        textAlign = Paint.Align.LEFT
    }

    override fun onDraw(canvas: Canvas?) {
        canvas.drawBitmap(getBitmap(70.dp, R.mipmap.psb), )


        canvas.drawText(text, )
    }

    fun getBitmap(width: Int, resId: Int): Bitmap {
        var option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, option)
        option.inJustDecodeBounds = false
        option.inDensity = option.outWidth
        option.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, resId, option)
    }
}