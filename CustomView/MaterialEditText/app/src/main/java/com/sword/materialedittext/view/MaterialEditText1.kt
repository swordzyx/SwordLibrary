package com.sword.materialedittext.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.sword.materialedittext.R
import com.sword.materialedittext.dp

private val TEXT_SIZE = 12.dp
private val TEXT_MARGIN = 8.dp
private val VERTICAL_OFFSET = TEXT_MARGIN + TEXT_SIZE + 3.dp
private val EXTRA_VERTICAL = 16.dp

class MaterialEditText1(context: Context, attrs: AttributeSet): AppCompatEditText(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
    }

    var shownFloatingLabel = false

    var shownFraction = 0f
        set(value){
            field  = value
            invalidate()
        }

    var useFloatingLabel = false
        set(value) {
            if (field != value) {
                field = value
                if (value) {
                    setPadding(paddingLeft,
                        (paddingTop + TEXT_SIZE + TEXT_MARGIN).toInt(), paddingRight, paddingBottom)
                } else {
                    setPadding(paddingLeft,
                        (paddingTop - TEXT_SIZE - TEXT_MARGIN).toInt(), paddingRight, paddingBottom)
                }
            }
        }

    private val animator by lazy {
        ObjectAnimator.ofFloat(this, "shownFraction", 0f, 1f)
    }

    init {
        /*
        * //runtime_symbol_list/debug/R.txt
        * MaterialEditText1 是一个数组，里面保存了该自定义组件中的所有的属性 ID
        * int[] styleable MaterialEditText1 { 0x7f030330 }
        * int styleable MaterialEditText1_useFloatingLabel 0
        * int attr useFloatingLabel 0x7f030330
        *
        * 下面其实可以直接传入一个数组。
        * */
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText1)
        /*
        * MaterialEditText1_useFloatingLabel 是属性在数组中的索引。其实可以传入 0
        * */
        useFloatingLabel = attributes.getBoolean(R.styleable.MaterialEditText1_useFloatingLabel, true)
        attributes.recycle()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (useFloatingLabel) {
            if(!shownFloatingLabel && !text.isNullOrEmpty()) {
                shownFloatingLabel = true
                animator.start()
            } else if(shownFloatingLabel && text.isNullOrEmpty()){
                shownFloatingLabel = false
                animator.reverse()
            }
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (shownFloatingLabel && useFloatingLabel) {
            paint.alpha = (shownFraction*0xff).toInt()
            canvas.drawText(hint.toString(),0, hint.length, paddingLeft.toFloat(), VERTICAL_OFFSET + EXTRA_VERTICAL * (1 - shownFraction), paint)
        }
    }
}