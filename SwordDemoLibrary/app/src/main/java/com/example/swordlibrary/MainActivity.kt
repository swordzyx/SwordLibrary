package com.example.swordlibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.webcontent.WebViewFragment

class MainActivity: AppCompatActivity() {
  private val webViewFragment = WebViewFragment(this)
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    setContentView(webViewFragment.createView())
  }


  override fun onBackPressed() {
    super.onBackPressed()
    webViewFragment.onBackPressed()
  }
}