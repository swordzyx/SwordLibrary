package com.example.swordlibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.webcontent.WebViewFragment
import com.sword.LogUtil
import com.sword.initWindowSize
import java.lang.Thread.sleep

class MainActivity: AppCompatActivity() {
  private val tag = "MainActivity"
  private lateinit var webViewFragment: WebViewFragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initWindowSize(this)
    
    /*webViewFragment = WebViewFragment(this)
    
    setContentView(webViewFragment.createView())
    webViewFragment.loadUrl("file:///android_asset/webView.html")

    webViewFragment.webview.callJavascript()*/

  }


  override fun onBackPressed() {
    super.onBackPressed()
    webViewFragment.onBackPressed()
  }
}