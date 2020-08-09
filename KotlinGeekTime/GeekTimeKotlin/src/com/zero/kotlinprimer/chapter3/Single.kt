package com.zero.kotlinprimer.chapter3

class StringUtils {
    companion object {
        fun isEmpty(str: String): Boolean{
            return "" == str
        }
    }
}

fun main(args: Array<String>){
    StringUtils.isEmpty("dknalkgdnf")
}
