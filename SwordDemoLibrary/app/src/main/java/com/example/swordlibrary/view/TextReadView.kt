package com.example.swordlibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.swordlibrary.R
import com.sword.LogUtil
import com.sword.createBitmap1
import com.sword.dp2px


/**
 * todo：文字无法选中
 * todo：文字中的换行符无效
 */
class TextReadView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val tag = "TextReadView"
    private val imageWidth = dp2px(100f)
    private val textContent =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut at dapibus eros. Cras vestibulum ligula et dolor fermentum, a cursus nibh tristique. Cras ut elit venenatis, scelerisque mi a, feugiat tortor. Vestibulum sit amet purus vitae mi dapibus venenatis a nec ligula. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Integer pretium urna a dolor malesuada vehicula. Vestibulum vestibulum, nunc vel interdum condimentum, metus diam luctus justo, at cursus velit sem non libero. Maecenas ut libero et eros molestie gravida. Sed aliquam est eget ex cursus imperdiet. Maecenas quis lacus volutpat nulla facilisis blandit eget et nisl. Nam sit amet finibus risus. Aliquam consequat feugiat metus vel varius. Pellentesque risus lacus, ornare non sem sed, commodo consectetur metus. Sed et nulla nisi. Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n Donec imperdiet nibh ut ornare egestas."

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = dp2px(15f)
    }
    private val measureResult = floatArrayOf(0f)
    private val bitmap =
        createBitmap1(resources, R.drawable.avatar_rengwuxian, imageWidth, imageWidth)
    private val imageMarginTop = dp2px(50f)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        LogUtil.debug(tag, "width: $w, height: $h")
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, width - paddingRight - imageWidth, imageMarginTop, paint)
        var textStartY = paddingTop - paint.fontMetrics.top
        var startTextCount = 0
        var textCount: Int

        while (startTextCount < textContent.length) {
            val maxWidth =
                if ((textStartY + paint.fontMetrics.bottom) > imageMarginTop && (textStartY + paint.fontMetrics.top) < (imageMarginTop + imageWidth)) {
                    width - paddingLeft - paddingRight - imageWidth
                } else {
                    width - paddingLeft - paddingRight
                }
            textCount = paint.breakText(
                textContent,
                startTextCount,
                textContent.length,
                true,
                maxWidth.toFloat(),
                measureResult
            )
            canvas.drawText(textContent, startTextCount, startTextCount + textCount, paddingLeft.toFloat(), textStartY, paint)
            startTextCount += textCount
            textStartY += paint.fontSpacing.toInt()
        }
    }
}