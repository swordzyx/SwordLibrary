package com.sword.base

import android.app.Application
import android.content.Context

class BaseApplication: Application() {
    companion object {
        private lateinit var currentApplication: Context

        fun currentApplication(): Context {
            return currentApplication
        }
    }
    override fun onCreate() {
        super.onCreate()
        currentApplication = this
    }
}