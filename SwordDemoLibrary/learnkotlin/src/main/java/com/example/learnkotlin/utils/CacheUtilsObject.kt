package com.example.learnkotlin.utils

import android.annotation.SuppressLint
import android.content.Context
import com.example.learnkotlin.R
import com.example.learnkotlin.core.BaseApplication

/**
 * CacheUtilsObject.save(..) 调用 save 方法
 * CacheUtilsObject.get(...) 调用 get 方法
 */
@SuppressLint("StaticFieldLeak")
object CacheUtilsObject {
    private val context = BaseApplication.currentApplication

    private val SP = context?.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun save(key: String, value: String) {
        SP?.edit()?.putString(key, value)?.apply()
    }

    fun get(key: String): String? {
        return SP?.getString(key, null)
    }


}