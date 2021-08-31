package com.sword.camerax

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.Preview

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun createPreviewUseCase() {
        val preview = Preview.Builder().build()
        val previewView = findViewById(R.id.preview_view)
    }
}