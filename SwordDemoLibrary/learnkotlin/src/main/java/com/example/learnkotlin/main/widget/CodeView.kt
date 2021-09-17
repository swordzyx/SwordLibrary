package com.example.learnkotlin.main.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.example.learnkotlin.R
import com.example.learnkotlin.utils.dp2px
import java.util.*

class CodeView : AppCompatTextView {
    constructor(context: Context) : super(context)
    
    @RequiresApi(Build.VERSION_CODES.M)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        gravity = Gravity.CENTER
        setBackgroundColor(context.getColor(R.color.colorPrimary))
        setTextColor(Color.WHITE)

        paint.apply { 
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = context.getColor(R.color.colorAccent)
            strokeWidth = dp2px(6f)
        }

        updateCode()
    }

    private val paint = Paint()

    private val codeList = arrayOf("kotlin", "android", "java", "http", "https", "okhttp", "retrofit", "tcp/ip")

    fun updateCode() {
        val random = Random().nextInt(codeList.size)
        val code = codeList[random]
        text = code
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), 0f, paint)
        super.onDraw(canvas)
    }
}