package com.example.swordlibrary.logic

import android.content.Context
import android.util.Patterns
import com.example.swordlibrary.data.SharedPreferencesUtils

class LoginLogic {
    fun login(context: Context, username: String, password: String): Boolean {
        return if (!isUserNameValid(username) || !isPasswordValid(password)) {
            false
        } else {
            //通过服务器判断账户及密码的有效性
            val result = checkFromServer(username, password)
            if (result) {
                //登录成功保持本地的信息
                SharedPreferencesUtils.put(context, username, password)
            }
            result
        }
    }

    internal fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.trim { it <= ' ' }.isNotEmpty()
        }
    }

    internal fun isPasswordValid(password: String): Boolean {
        return password.trim { it <= ' ' }.length > 5
    }

    companion object {
        // 为了进行演示，去除通过服务器鉴定的逻辑，当用户输入特定账号及密码为时则验证成功
        internal fun checkFromServer(username: String, password: String): Boolean {
            return username == "123@163.com" && password == "123456"
        }
    }
}