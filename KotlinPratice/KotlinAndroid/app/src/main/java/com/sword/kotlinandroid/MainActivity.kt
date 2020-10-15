package com.sword.kotlinandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.sword.base.utils.CacheUtils
import com.sword.kotlinandroid.entity.User
import com.sword.kotlinandroid.widget.CodeView

class MainActivity: AppCompatActivity(), View.OnClickListener {
    private val usernameKey: String = "username"
    private val passwordKey: String = "password"

    private lateinit var et_username: EditText
    private lateinit var et_password: EditText
    private lateinit var et_code: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_username = findViewById(R.id.et_username)
        et_password = findViewById(R.id.et_password)
        et_code = findViewById(R.id.et_code)

        et_username.setText(CacheUtils.get(usernameKey))
        et_password.setText(CacheUtils.get(passwordKey))

        val btn_login: Button = findViewById(R.id.btn_login)
        val img_code: CodeView = findViewById(R.id.code_view)

        btn_login.setOnClickListener(this)
        img_code.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v is CodeView) {
            v.updateCode()
        } else if(v is Button) {
            login()
        }
    }

    private fun login() {
        val username: String = et_username.text.toString()
        val password: String = et_password.text.toString()
        val code: String = et_code.text.toString()

        val user: User = User(username, password, code)

        if(verify(user)) {
            CacheUtils.save(usernameKey, username);
            CacheUtils.save(passwordKey, password);
            startActivity(Intent(this, LessonActivity::class.java));
        }

    }
}