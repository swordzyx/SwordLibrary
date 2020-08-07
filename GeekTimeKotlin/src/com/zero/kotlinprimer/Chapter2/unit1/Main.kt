package com.zero.kotlinprimer.Chapter2.unit1

fun print(str: String = "Zhang Tao"): String?{
    println("$str")
    return str
}

fun main(arg: Array<String>){
    /*print("hello")
    print()*/
    function()
}

fun function(){
    var str = "hello"

    fun say(count: Int = 10){
        println(str)
        if(count > 0){
            say(count - 1)
        }
    }

    say()

}