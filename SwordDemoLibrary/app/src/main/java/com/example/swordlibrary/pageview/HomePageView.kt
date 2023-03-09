package com.example.swordlibrary.pageview

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView

class HomePageView(val context: Context): PageView {
  override val rootView: View
    get() = createView()
  private fun createView(): View {
    return LinearLayout(context).apply {
      contentDescription = "首页"
      val textView = AppCompatTextView(context).apply { 
        text = "登录成功"
        gravity = Gravity.CENTER
      }
      addView(textView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
    }
  }

  
}