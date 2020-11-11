package com.sword.customviewset

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import kotlin.concurrent.thread

class ViewDrawAnalyse : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_draw_analyse)


//        MotionEvent

        val textView = findViewById<TextView>(R.id.textView)
        textView.post {

        }
    }
}