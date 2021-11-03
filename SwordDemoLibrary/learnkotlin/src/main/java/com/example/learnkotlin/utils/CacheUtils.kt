/**
 * @file:JvmName("CacheUtils") 签名的 @file 表示这个注解作用于文件
 */
@file:JvmName("CacheUtils")
package com.example.learnkotlin.utils

import android.annotation.SuppressLint
import android.content.Context
import com.example.learnkotlin.R
import com.example.learnkotlin.core.BaseApplication

/**
 * ShareaPreference 工具类
 */
//返回的并非应用组建的 Context，这里应该不会导致内存泄漏
@SuppressLint("StaticFieldLeak")
private val context = BaseApplication.currentApplication

private val SP = context?.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

/**
 * Java 通过 CacheUtils.save(key, value) 即可调用 
 */
@SuppressLint("CommitPrefEdits")
fun save(key: String, value: String) = SP?.edit()?.putString(key, value)?.apply()

/**
 * 像下面这种函数体中只有一行代码的，可以直接用"="连接起来
        fun get(key: String): String? {
            return SP?.getString(key, null)
        }
 * 上面的代码等价于：fun get(key: String): String? = SP?.getString(key, null)
 *
 * 基于 Kotlin 的类型推断，函数后面的返回值类型声明也可以省略掉，因此上面的代码可以再次简化：
 *  fun get(key: String) = SP?.getString(key, null)
 */
fun get(key: String) = SP?.getString(key, null)