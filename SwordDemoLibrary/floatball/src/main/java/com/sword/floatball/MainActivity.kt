package com.sword.floatball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    //FloatBall2.getInstance(this).showFloatBall()
    //FloatBall.getInstance(this).showFloatBall()
  }
}