package com.example.learnkotlin.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context



class BaseApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = base
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        fun currentApplication(): Context? {
            return context
        }
    }
}