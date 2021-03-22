package com.example.hencodersourcecode

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup

fun Activity.drapBadge() {
    val view = View(this)
    view.setBackgroundColor(Color.RED)
    val dacorView = window.decorView as ViewGroup
    dacorView.addView(view, 200, 200)
}
