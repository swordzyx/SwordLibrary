package com.example.learnkotlin.core

interface BaseView<T> {
    /**
     * Java 中只有抽象方法，而没有抽象属性，因此只能在别的类中创建 T 的实例，然后实现 getPresenter() 接口返回创建的 T 的实例
     */
    //fun getPresenter(): T

    val presenter: T
}