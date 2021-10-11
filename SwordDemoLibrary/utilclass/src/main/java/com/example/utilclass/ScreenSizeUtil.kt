package com.example.utilclass

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

class ScreenSizeUtil {
    /**
     * 
     */
    fun getScreenSizeByWindowManager(context: Context): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        return Point().apply {
            display.getSize(this)
        }
    }
}