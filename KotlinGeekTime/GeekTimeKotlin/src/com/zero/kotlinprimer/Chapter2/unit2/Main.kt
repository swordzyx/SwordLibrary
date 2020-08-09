package com.zero.kotlinprimer.Chapter2.unit2

import java.io.File

fun main(args: Array<String>){
    /*var file = File("GeekTimeKotlin.iml")
    println(file.readText())*/

    var dog = Dog()
    dog.printName(dog)
}

open class Animal
class Dog: Animal()

fun Animal.name() = "animal"
fun Dog.name() = "Dog"

fun Animal.printName(anim: Animal){
    println(anim.name())
}
