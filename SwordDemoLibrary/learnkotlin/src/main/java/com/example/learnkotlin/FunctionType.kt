package com.example.learnkotlin

import android.util.Log

class View {
    interface OnClickListener {
        fun onClick()
    }
    
    fun setOnClickListener(listener: OnClickListener) {
        listener.onClick()
    }
    
    fun setOnClickListener(onClick: () -> Unit) {
        onClick.invoke()
    }
}

fun main() {
    /**
     * 对于下面这种场景，我们实际需要的不是 View.OnClickListener 这个对象，而是里面的 onClick() 函数。也就是说我们实际想要的是 View().setOnClickListener(::onClick)，传入函数的引用，之后在需要的直接执行传入的函数即可，不能通过 view.setOnClickListener(onClick(view)) 的形式来传入函数类型的参数，因为这会直接将作为参数的函数的调用结果传进去，因此只能传入函数的引用。
     * kotlin 中允许直接将函数作为参数传入到另一个函数中。Java 中是不允许传函数的，所以只能将函数包在一个对象中，然后将对象作为参数传到目标函数中，间接的将函数传过去。
     * 
     * 一个函数类型主要分为两部分，输入和输入，kotlin 中通过以下方式定义函数类型的参数： (输入的类型) -> 输出类型
     */
    View().setOnClickListener(object : View.OnClickListener {
        override fun onClick() {
            Log.d("sword", "onClick")
        }
    })

    /**
     * 使用函数类型的参数简化上面的代码，传入 onClick() 函数的引用
     */
    View().setOnClickListener(::onClick)


    /**
     * 传递匿名函数
     */
    View().setOnClickListener(fun () {
        Log.d("sword","传递匿名函数")
    })

    /**
     * 通过 Lambda 形式传递函数 
     */
    View().setOnClickListener {
        Log.d("sword", "通过 Lambda 形式传递函数")
    }
     
}

fun onClick() {
    Log.d("sword", "onClick")
}

fun measureTime(action: () -> Unit) {
    println(">>>>>>")
    val start = System.currentTimeMillis()
    
    action()
    
    val end = System.currentTimeMillis()
    println("<<<<<< [${end - }]")
    
    
}