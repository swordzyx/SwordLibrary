package com.example.swordlibrary

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.view.*
import com.example.swordlibrary.webcontent.WebViewFragment
import com.sword.initWindowSize

class MainActivity2: AppCompatActivity() {
  private val tag = "MainActivity2"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main2)
    initWindowSize(this)


  }
}