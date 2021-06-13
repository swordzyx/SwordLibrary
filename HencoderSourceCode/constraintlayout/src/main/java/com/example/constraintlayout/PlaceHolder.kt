package com.example.constraintlayout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Placeholder

class PlaceHolder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_holder)
    }

    fun onClick(view: View) {
        findViewById<Placeholder>(R.id.placeholder).setContentId(view.id)
    }
}
