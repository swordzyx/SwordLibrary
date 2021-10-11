package com.example.learnkotlin

/**     函数声明
 * kotlin 中声明函数使用的时 fun 关键字
 * koltin 函数参数类型位于参数的右边，用 : 分隔
 * kotlin 函数返回值类型位于函数参数的右边，用 “:” 分隔，并且返回值为空的时候，可以省略 “:” 和返回值类型，空返回的类型是 Unit
 */


/**    变量声明
 * 变量声明使用的是 var 或者 val 关键字，变量类型位于变量名的右边，var 表示可读可写变量，val 表示只能赋值一次的变量。
 * val 其实对应 Java 中的 final 关键字
 *     创建对象
 * kotlin 创建对象只需调用对象的构造器即可，无需任何关键字。
 *     类型推断
 * kotlin 中，如果表达式等号的右边的类型可以让编译器知道，那么表达式左边的变量类型是可以通过“类型推断”推断出来的，而表达式左边的类型声明就可以去掉。
 * 示例：
 * var age: Int = 18  等价于
 * var age = 18
 */

//无参，无返回值函数。Java 中无返回值的函数使用 void 声明，而 Kotlin 中使用的时 Unit
//下面的声明也等价于 fun main(): Unit {...}
fun main() {
    println("Hello world")
    
    //变量声明
    var age: Int = 18
    val name: String = "kotlin"
    //创建对象
    var java: Java = Java()
    
    //类型推断，上面三句变量的声明等价于以下三行
    var age1 = 18
    val name1 = "kotlin"
    var java1 = Java()
}

//有参，有返回值函数
fun doubleNumber(x: Int): Int {
    return 2 * x
}


fun test() {
    /**
     * 输入 isVerify().if 会自动生成以下代码
     */
    if (isVerify()) {
        TODO()
    }
}

fun isVerify() : Boolean{
    return false;
}

