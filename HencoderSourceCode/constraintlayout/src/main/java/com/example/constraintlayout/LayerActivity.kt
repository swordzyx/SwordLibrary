package com.example.constraintlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.helper.widget.Layer
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible

class LayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitry_layer)

        //点击按钮将四个 ImageView 一起设为不可见
        findViewById<Button>(R.id.button).setOnClickListener { view ->
            findViewById<Layer>(R.id.layer).rotation = 45f
            findViewById<Layer>(R.id.layer).translationX = 100f
            findViewById<Layer>(R.id.layer).translationY = 100f
        }
    }
}