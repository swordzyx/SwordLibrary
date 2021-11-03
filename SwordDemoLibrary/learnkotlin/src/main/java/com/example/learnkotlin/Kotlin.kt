package com.example.learnkotlin

import com.example.learnkotlin.entity.User

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
    //repeat 实际是一个函数，接收两个参数，第一个是循环的次数，第二个是一个 Lambda，这个 Lambda 接收一个参数
    repeat(100) {
        println(it)
    }

    //循环 i 从 0 到 99，包含两个左右边界
    for (i in 0..99) {

    }

    //数组遍历
    val arrayOf = arrayOf(1, 3, 4, 5, 34, 5, 53, 2, 3, 43, 53, 32, 34)
    for (i in arrayOf.indices) {

    }

}

fun dataClass() {
    /*
    User 是一个 data class，kotlin 会自动生成一个 copy 函数，将一个对象中的数据复制到另一个对象中，这样就不必一个一个成员的去赋值了
    user2 和 user 的成员的值是一样的
     */
    val user = User("", "", "")
    val user2 = user.copy()
    //kotlin 中，== 比较的是对象中的值，它实际回调用 equals 函数。=== 在 kotlin 中比较的就是对象的地址
    println(user2 == user)

    val user3 = User("kjdnk", "kdljf", "kjfdk")

    //在 Java 中只能通过以下方式来获取 user3 中的成员属性
    val username = user3.username
    val password = user3.password
    val code = user3.code
    //而在 kotlin 中可以直接以结构的方式直接获取 user3 中的成员属性,解构获取的值与构造器上的值是一一对应的
    val (name3: String, pass3: String, code3: String) = user3
    println("解构 username=$name3, password=$pass3, code=$code3")
}

fun main2() {
    //变量声明
    var age: Int = 18
    val name: String = "kotlin"
    //创建对象
    var java: Java = Java()

    //类型推断，上面三句变量的声明等价于以下三行
    var age1 = 18
    val name1 = "kotlin"
    var java1 = Java()

    println("Hello world")
    val s = """
        line1
        line2
        line3
    """.trimIndent()
    println(s)
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

fun isVerify(): Boolean {
    return false;
}

open class A {
    open fun test() {

    }
}

class B : A() {
    override fun test() {

    }
}

