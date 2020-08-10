package com.zero.kotlinprimer.Chapter2.unit3

val print = {name: String ->
    println(name)
}

val lambdaA = {a: Int, b: Int, c: Int, d: Int, e: Int, f: Int, g: Int, h: Int, i: Int, j: Int, k: Int, l: Int, m: Int, n: Int, o: Int, p: Int, q: Int, r: Int, s: Int, t: Int, u: Int, v: Int, w: Int, x: Int ->
    print("Zhang Tao")
}

fun main(args: Array<String>){
//    print("Zhang Tao");
    lambdaA(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
}





