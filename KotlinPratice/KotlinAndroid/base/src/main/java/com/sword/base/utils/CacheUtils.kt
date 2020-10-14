package com.sword.base.utils

import android.content.Context
import android.content.SharedPreferences
import com.sword.base.BaseApplication
import com.sword.base.R

object CacheUtils {
    private var context: Context = BaseApplication.currentApplication();

    private var SP: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun save(key: String, value: String) {
        SP.edit().putString(key, value).apply();
    }

    fun get(key: String): String? {
        return SP.getString(key, null)
    }
}