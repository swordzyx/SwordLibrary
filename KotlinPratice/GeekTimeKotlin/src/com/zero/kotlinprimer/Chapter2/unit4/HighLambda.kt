package com.zero.kotlinprimer.Chapter2.unit4

inline fun onlyif(isDebug: Boolean, block: () -> Unit){
    if(isDebug) block()
}

fun main(args: Array<String>){
    onlyif(true){
        println("打印日志")
    }

    /*val runnable = Runnable{
        println("Runnable::run")
    }

    val function: () -> Unit

    function = runnable::run

    onlyif(true, function)*/
}