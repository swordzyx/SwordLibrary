package sword.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import sword.SwordLog
import sword.dp2px

class TextReadViewStaticLayout(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val tag = "TextReadViewStaticLayout"
    private val textContent =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut at dapibus eros. Cras vestibulum ligula et dolor fermentum, a cursus nibh tristique. Cras ut elit venenatis, scelerisque mi a, feugiat tortor. Vestibulum sit amet purus vitae mi dapibus venenatis a nec ligula. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Integer pretium urna a dolor malesuada vehicula. Vestibulum vestibulum, nunc vel interdum condimentum, metus diam luctus justo, at cursus velit sem non libero. Maecenas ut libero et eros molestie gravida. Sed aliquam est eget ex cursus imperdiet. Maecenas quis lacus volutpat nulla facilisis blandit eget et nisl. Nam sit amet finibus risus. Aliquam consequat feugiat metus vel varius. Pellentesque risus lacus, ornare non sem sed, commodo consectetur metus. Sed et nulla nisi. Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n Praesent tincidunt risus scelerisque"

    init {
       isVerticalScrollBarEnabled = true
    }

    private val textPaint = TextPaint().apply {
        textSize = dp2px(12f)
    }
    private var staticLayout: StaticLayout? = null
    //val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        if (staticLayout == null) {
            staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(textContent, 0, textContent.length, textPaint, measureWidth - paddingLeft - paddingRight).build()
            } else {
                StaticLayout(textContent, textPaint, measureWidth - paddingLeft - paddingRight, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
            }
        }

        SwordLog.debug(tag, "width: $measureWidth, height: ${staticLayout!!.height}")
        setMeasuredDimension(measureWidth, staticLayout!!.height)
    }

    override fun onDraw(canvas: Canvas) {
        staticLayout?.draw(canvas)
    }


}