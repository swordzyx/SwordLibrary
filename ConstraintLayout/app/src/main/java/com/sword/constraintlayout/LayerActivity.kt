package com.sword.constraintlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.layer.*

class LayerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layer)
        buttonLayer.setOnClickListener{
            layer.rotation = 45f
            layer.translationX = 100f
            layer.translationY = 100f
        }

    }
}