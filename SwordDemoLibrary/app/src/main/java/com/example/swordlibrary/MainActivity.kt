package com.example.swordlibrary

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.example.swordlibrary.pageview.HomePageView
import com.example.swordlibrary.pageview.LoginPageView
import com.example.swordlibrary.view.*
import com.sword.LogUtil
import com.sword.initWindowSize
import com.sword.toast

class MainActivity: AppCompatActivity(), LoginPageView.LoginListener {
  private val tag = "MainActivity"
  //private lateinit var webViewFragment: WebViewFragment
  private lateinit var contentView: ViewGroup
  
  private val homePageView = HomePageView(this)
  private val loginPageView = LoginPageView(this, this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //setContentView(R.layout.activity_main)
    setContentView(loginPageView.rootView)
    
    contentView = window.decorView.findViewById(android.R.id.content)
    contentView.forEach { view -> 
      LogUtil.debug(tag, view.javaClass.name)
    }
    initWindowSize(this)

    //setContentView(webViewFragment.createView())

    /*
     * MaterialEditText 测试：
     * 1. 启用 enableFloatLabel，编辑框没有内容时，floatLabel 向下逐渐隐藏，最后消失，编辑框有内容时，floatLabel 向上逐渐显示
     * 2. 禁用 enableFloatLabel，不显示 FloatLabel
     */
  }

  private fun testAnimation() {
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

    //启动动画
    //circleView.startAnimate()
    //flipPageView.startSequentiallyAnimate()
    //pointView.startAnimator()
    provinceView.startAnimator()
    flipPageView.startSequentiallyAnimate()
  }


  override fun onBackPressed() {
    super.onBackPressed()
    //webViewFragment.onBackPressed()
  }

  override fun loginSuccess(username: String) {
    contentView.removeView(loginPageView.rootView)
    contentView.addView(homePageView.rootView)
  }

  override fun loginFailed(username: String, msg: String) {
    toast(this, "$username 登录失败", Toast.LENGTH_LONG)
    window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
      
    }
  }
}