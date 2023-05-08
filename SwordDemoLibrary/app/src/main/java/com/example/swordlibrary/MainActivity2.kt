package com.example.swordlibrary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.view.*
import com.sword.initWindowSize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity2: AppCompatActivity() {
  private val tag = "MainActivity2"
  private var multiTouchView1: MultiTouchView1? = null
  private var multiTouchView2: MultiTouchView2? = null
  private var multiTouchView3: MultiTouchView3? = null

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
        if (multiTouchView1 == null) {
          multiTouchView1 = MultiTouchView1(
            this@MainActivity2)
          contentView.addView(multiTouchView1,
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          )
        }

        hideAllView()
        if (multiTouchView1?.visibility != View.VISIBLE) {
          multiTouchView1?.visibility = View.VISIBLE
        }
      } 
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))


    rootView.addView(Button(this).apply {
      text = "MultiTouchView2 接力型滑动"
      setOnClickListener {
        contentView.visibility = View.VISIBLE
        if (multiTouchView2 == null) {
          multiTouchView2 = MultiTouchView2(
            this@MainActivity2)
          contentView.addView(multiTouchView2,
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          )
        }

        hideAllView()
        if (multiTouchView2?.visibility != View.VISIBLE) {
          multiTouchView2?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

    rootView.addView(Button(this).apply {
      text = "MultiTouchView3 协作型滑动"
      setOnClickListener {
        contentView.visibility = View.VISIBLE
        
        if (multiTouchView3 == null) {
          multiTouchView3 = MultiTouchView3(
            this@MainActivity2)
          contentView.addView(multiTouchView3,
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          )
        }

        hideAllView()
        if (multiTouchView3?.visibility != View.VISIBLE) {
          multiTouchView3?.visibility = View.VISIBLE
        }
      }
    }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    
    initWindowSize(this)
  }

  private fun hideAllView() {
    if (multiTouchView1?.visibility != View.GONE) {
      multiTouchView1?.visibility = View.GONE
    }

    if (multiTouchView2?.visibility != View.GONE) {
      multiTouchView2?.visibility = View.GONE
    }

    if (multiTouchView3?.visibility != View.GONE) {
      multiTouchView3?.visibility = View.GONE
    }
  }

  override fun onBackPressed() {
    if (contentView.visibility != View.GONE) {
      contentView.visibility = View.GONE
    } else {
      super.onBackPressed()
    }
  }
}