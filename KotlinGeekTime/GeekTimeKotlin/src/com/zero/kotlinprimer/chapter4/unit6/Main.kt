package com.zero.kotlinprimer.chapter4.unit6

fun main(args: Array<String>){
    val user = User(1, "zero", PlayerViewType.GREEN)

    PlayUI().showUI(user)
}