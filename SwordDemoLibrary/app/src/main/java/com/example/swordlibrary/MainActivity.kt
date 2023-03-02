package com.example.swordlibrary

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.view.CircleView
import com.example.swordlibrary.view.FlipPageView
import com.example.swordlibrary.view.PointView
import com.example.swordlibrary.view.ProvinceView
import com.example.swordlibrary.webcontent.WebViewFragment
import com.sword.initWindowSize

class MainActivity: AppCompatActivity() {
  private val tag = "MainActivity"
  private lateinit var webViewFragment: WebViewFragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val circleView = CircleView(this)
    val flipPageView = FlipPageView(this)
    val pointView = PointView(this)
    val provinceView = ProvinceView(this)

    val linearLayout = LinearLayout(this).apply {
      val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
      //addView(circleView)
      orientation = VERTICAL
      addView(flipPageView, params)
      addView(provinceView, params)
      //addView(pointView)
    }

    setContentView(linearLayout)
    initWindowSize(this)

    //启动动画
    //circleView.startAnimate()
    //flipPageView.startSequentiallyAnimate()
    //pointView.startAnimator()
    provinceView.startAnimator()
    flipPageView.startSequentiallyAnimate()


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