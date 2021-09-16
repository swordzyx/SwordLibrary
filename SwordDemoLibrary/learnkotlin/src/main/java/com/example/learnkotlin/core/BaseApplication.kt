package com.example.learnkotlin.core

import android.app.Application
import android.content.Context



class BaseApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = base
    }

    companion object {
        private var context: Context? = null

        fun currentApplication(): Context? {
            return context
        }
    }
}