package com.example.learnkotlin

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET

interface API {
    @GET("lessons")
    fun lessons(): Call<Any>
}

val RETROFIT = Retrofit.Builder()
    .baseUrl("https://api.hencoder.com")
    .build()

inline fun <reified T> create(): T {
    return RETROFIT.create(T::class.java)
}

fun main() {
    /**
     * 每次调用 create() 方法时都需要把 class 对象传过去，但实际我们需要的只是 API 这个类型而已，也就是说 ::class.java 不是我们所需要，但是又必须要传。
     *
     * 但仅仅只将 API 这个类型传过去是不行的，因为无法直接通过泛型 T 获取到 T 的 class 对象，T::class.java 这种写法无法通过编译。
     *
     * 但是 kotlin 中的 inline ，inline 会把函数题中的代码插入到每一个调用处，而代码插入到调用处，就能知道调用方使用的是什么类型了。即，当 create 方法使用 inline 修饰时，下面在调用 create 方法时会直接将 create 中的代码 "RETROFIT.create(clazz)" 拷贝到 main 方法中，这时 "RETROFIT.create" 就能知道传进来的是什么类型了。这时 create(API) 这句代码是可以正常执行的。
     */
    println(create<API>())
}