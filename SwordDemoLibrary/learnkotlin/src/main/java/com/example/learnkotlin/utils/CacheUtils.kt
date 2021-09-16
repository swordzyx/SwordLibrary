@file:JvmName("CacheUtils")
package com.example.learnkotlin.utils

import android.annotation.SuppressLint
import android.content.Context
import com.example.learnkotlin.R
import com.example.learnkotlin.core.BaseApplication

//返回的并非应用组建的 Context，这里应该不会导致内存泄漏
@SuppressLint("StaticFieldLeak")
private val context = BaseApplication.currentApplication()

private val SP = context?.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

fun save(key: String, value: String) {
    SP?.edit()?.putString(key, value)?.apply()
}

fun get(key: String): String? {
    return SP?.getString(key, null)
}