package com.sword.customviewset

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TouchDragActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.drag_helper_grid_view)
        setContentView(R.layout.drag_up_down)
    }
}