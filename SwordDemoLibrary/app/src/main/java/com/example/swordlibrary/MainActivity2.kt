package com.example.swordlibrary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.VERTICAL
import android.widget.MultiAutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.view.*
import com.example.swordlibrary.webcontent.WebViewFragment
import com.sword.initWindowSize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity2: AppCompatActivity() {
  private val tag = "MainActivity2"

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)
    val rootView = findViewById<LinearLayout>(R.id.rootView)
    val contentView = findViewById<FrameLayout>(R.id.contentView)
    rootView.addView(Button(this).apply {
      text = "MultiTouchView1 单点触控"
      setOnClickListener {
        contentView.visibility = View.VISIBLE
        contentView.addView(MultiTouchView1(
          this@MainActivity2), 
          FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )
      } 
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))


    rootView.addView(Button(this).apply {
      text = "MultiTouchView1 接力型滑动"
      setOnClickListener {
        contentView.visibility = View.VISIBLE
        contentView.addView(MultiTouchView2(
          this@MainActivity2),
          FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    
    initWindowSize(this)
  }

  override fun onBackPressed() {
    if (contentView.visibility != View.GONE) {
      contentView.visibility = View.GONE
    } else {
      super.onBackPressed()
    }
  }
}