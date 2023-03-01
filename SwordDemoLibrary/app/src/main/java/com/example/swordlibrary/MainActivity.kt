package com.example.swordlibrary

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.view.CircleView
import com.example.swordlibrary.view.FlipPageView
import com.example.swordlibrary.webcontent.WebViewFragment
import com.sword.initWindowSize

class MainActivity: AppCompatActivity() {
  private val tag = "MainActivity"
  private lateinit var webViewFragment: WebViewFragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val circleView = CircleView(this)
    val flipPageView = FlipPageView(this)
    val linearLayout = LinearLayout(this).apply {
      addView(circleView)
      //addView(flipPageView)
    }

    setContentView(linearLayout)
    initWindowSize(this)

    //启动动画
    circleView.startAnimate()
    //flipPageView.startSequentiallyAnimate()


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