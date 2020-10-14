package com.zero.kotlinprimer.Chapter1

import com.zero.kotlinprimer.Chapter1.B.format
import kotlin.reflect.KClass

/**
 * kotlin 基本语法
 */

var age:  Int = 18
var name: String = "Zhang Tao"
var name2: String? = null

fun main(args: Array<String>){
    //name = name2 会报错
//    name = name2!!
//    printLen(name)
//
//    testClass(JavaMain::class.java) //调用 java 的 class 对象
//    testClass(KotlinMain::class)//调用 kotlin 的 class 对象

//    A.a.putNumber(123);

//    function("")
    val age = 18
    val name = "Zhang Tal"

//    println("我叫 %d，我今年 %d 岁", name, age)
}

fun printLen(str: String): String{
    println("这个字符串是：$str")
    return str
}

fun testClass(clazz: Class<JavaMain>){
    println(clazz.simpleName)
}

fun testClass(clazz: KClass<KotlinMain>){
    println(clazz.simpleName)
}

fun function(str: String){
    var fmt1 = format(str)
    println(fmt1.length)
//    var fmt2: String = format(str)
    var fmt3: String? = format(str)

//    println("$fmt1 + $fmt2 + $fmt3")
}

object Utils{
    @JvmStatic
    fun sayMessage(msg: String?){
        println("$msg")
    }
}