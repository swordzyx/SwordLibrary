package com.sword.materialedittext.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText
import com.sword.materialedittext.R
import com.sword.materialedittext.dp

private val TEXT_MARGIN = 8.dp
private val TEXT_SIZE = 12.dp
private val VERTICAL_OFFSET = 23.dp
private val EXTRA_VERTICAL_OFFSET = 16.dp

class MaterialEditText(context: Context, attrs: AttributeSet): AppCompatEditText(context, attrs){

    //控制提示文字的透明度
    var floatingLabelFraction = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
    }

    private val animator by lazy {
        ObjectAnimator.ofFloat(this, "floatingLabelFraction", 1f, 0f)
    }

    var floatingLabelShown = false

    //控制整个 View 的大小，动画的总开关
    var useFloatingLabel = false
        set(value) {
            if (field != value) {
                field = value
                if(value) {
                    setPadding(paddingLeft, (paddingTop + TEXT_MARGIN + TEXT_SIZE).toInt(), paddingRight, paddingBottom)
                } else if(!value){
                    setPadding(paddingLeft, (paddingTop - TEXT_MARGIN - TEXT_SIZE).toInt(), paddingRight, paddingBottom)
                }
            }
        }

    init {
        val value = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)
        useFloatingLabel = value.getBoolean(R.styleable.MaterialEditText_floatingLabelShown, true)
        value.recycle()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (useFloatingLabel){
            if (floatingLabelShown && text.isNullOrEmpty()) {
                floatingLabelShown = false
                animator.start()
            } else if (!floatingLabelShown && !text.isNullOrEmpty()) {
                floatingLabelShown = true
                animator.reverse()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(useFloatingLabel){
            paint.alpha = (floatingLabelFraction * 0xff).toInt()
            canvas.drawText(hint.toString(), 0, hint.length, paddingLeft.toFloat(), VERTICAL_OFFSET  + EXTRA_VERTICAL_OFFSET * (1 - floatingLabelFraction), paint)
        }
    }
}