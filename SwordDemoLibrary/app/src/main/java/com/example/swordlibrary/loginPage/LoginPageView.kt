package com.example.swordlibrary.loginPage

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.swordlibrary.utils.Theme
import com.sword.dp

class LoginPageView(val context: Context) {
  private val marginHorizontal = dp(10)
  
  private val rootView by lazy {
    initView()
  }


  private fun initView(): View {
    return LinearLayout(context).apply {
      val layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).apply {
        gravity = Gravity.CENTER_HORIZONTAL
        leftMargin = marginHorizontal
        rightMargin = marginHorizontal
      }
      
      
      val userNameEditText = EditText(context).apply {
        hint = "请输入手机号/用户名"
      }
      addView(userNameEditText, layoutParams)
      
      val passwordEditText = EditText(context).apply {
        hint = "请输入登录密码"
      }
      addView(passwordEditText, layoutParams)
      
      val loginButton = Button(context).apply { 
        text = "登录"
        //background =
      }
    }

  }

}