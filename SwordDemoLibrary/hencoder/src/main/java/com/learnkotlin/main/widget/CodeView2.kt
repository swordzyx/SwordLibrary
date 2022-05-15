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

/**
 * 将两个参数的构造器声明为主构造器，而原本主构造器中的初始化代码放到 init{...} 代码块中
 * 原本主构造器中调用的父类的构造器也要放到类声明出的父类后面，不需要 super
 */
@RequiresApi(Build.VERSION_CODES.M)
class CodeView2 constructor(context: Context, attrs: AttributeSet?)  : AppCompatTextView(context, attrs)  {

    //主构造器之外的构造器需要通过 this 显式的调用主构造器
    constructor(context: Context) : this(context, null)

    /*@RequiresApi(Build.VERSION_CODES.M)
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
    }*/

    /**
     * kotlin 会将 init 代码块和成员属性的初始化是按照代码在文件中的上下顺序一起放到构造函数中，因此 paint 和 codeList 的初始化全部放到 init 代码块的上面
     */
    private val paint = Paint()

    private val codeList = arrayOf("kotlin", "android", "java", "http", "https", "okhttp", "retrofit", "tcp/ip")
    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        gravity = Gravity.CENTER
        setBackgroundColor(context.getColor(R.color.colorPrimary))
        setTextColor(Color.WHITE)

        paint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = context.getColor(R.color.colorAccent)
            strokeWidth = 6f.dp2px()
        }

        updateCode()
    }

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