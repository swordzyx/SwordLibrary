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
 * 用默认值的方式可省略单参数的构造函数，然后将双参数的构造方法声明为主构造函数，初始化代码放到 init 代码块中
 */
@RequiresApi(Build.VERSION_CODES.M)
class  CodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {
    //实名函数参数默认值的方式可以将第二个参数默认设为 null，因此无需再声明单参数的构造方法
    //constructor(context: Context) : super(context)


    private val paint = Paint()

    private val codeList = arrayOf("kotlin", "android", "java", "http", "https", "okhttp", "retrofit", "tcp/ip")
    /*
    双参数的构造方法声明为主构造函数之后，初始化代码放在 init 代码块中，由于 init 代码块用到了 paint 成员，因此 paint 的声明和初始化需要放到 init 代码块之前
    @RequiresApi(Build.VERSION_CODES.M)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) */
    init {
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