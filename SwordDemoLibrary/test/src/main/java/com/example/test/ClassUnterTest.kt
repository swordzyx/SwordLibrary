package com.example.test

import android.content.Context

class ClassUnterTest(val context: Context) {
    fun getHelloWorldString(): String {
        return context.getString(R.string.hello_world)
    }
}