package com.example.swordlibrary.PageView

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.example.swordlibrary.logic.LoginLogic
import com.sword.dp
import com.example.swordlibrary.theme.Theme

class LoginPageView(val context: Context) : PageView {
    private val marginHorizontal = dp(10)
    private val loginLogic = LoginLogic()
    var loginListener: LoginListener? = null


    private val rootView by lazy {
        initView()
    }

    override fun initView(): View {
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

            val loginButton = Theme.createCircleButton(context).apply {
                text = "登录"
                setOnClickListener {
                    val username = userNameEditText.text.toString()
                    if (loginLogic.login(
                            username,
                            passwordEditText.text.toString()
                        )
                    ) {
                        loginListener?.loginSuccess(username)
                    } else {
                        loginListener?.loginFailed("登录失败", username)
                    }
                }
            }
            addView(loginButton)
        }

    }

    interface LoginListener {
        fun loginSuccess(username: String){}
        fun loginFailed(username: String, msg: String){}
    }

}