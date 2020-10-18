package com.sword.base

import android.app.Application
import android.content.Context

class BaseApplication: Application() {
    companion object {
        @get:JvmName("currentApplication")
        lateinit var currentApplication: Context
            private set
    }
    override fun onCreate() {
        super.onCreate()
        currentApplication = this
    }
}