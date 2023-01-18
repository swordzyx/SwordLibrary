package com.example.swordlibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.webcontent.WebViewFragment

class MainActivity: AppCompatActivity() {
  private lateinit var webViewFragment: WebViewFragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    webViewFragment = WebViewFragment(this)
    
    setContentView(webViewFragment.createView())
    webViewFragment.loadUrl("file:///android_asset/webview.html")
  }


  override fun onBackPressed() {
    super.onBackPressed()
    webViewFragment.onBackPressed()
  }
}