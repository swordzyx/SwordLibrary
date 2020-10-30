package com.sword.customviewset.view.drawable

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.sword.customviewset.drawable.MeshDrawable
import com.sword.customviewset.view.dp

class DrawableView(context: Context, attrs: AttributeSet): View(context, attrs) {
    val drawable = MeshDrawable()

    override fun onDraw(canvas: Canvas) {
        drawable.setBounds(50.dp.toInt(), 80.dp.toInt(), width, height)
        drawable.draw(canvas)
    }

}