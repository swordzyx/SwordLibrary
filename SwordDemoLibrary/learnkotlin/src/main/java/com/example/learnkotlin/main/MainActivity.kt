package com.example.learnkotlin.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.learnkotlin.R
import com.example.learnkotlin.entity.User
import com.example.learnkotlin.lesson.LessonActivity
import com.example.learnkotlin.main.widget.CodeView
import com.example.learnkotlin.utils.get
import com.example.learnkotlin.utils.save
import com.example.learnkotlin.utils.toast

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val usernameKey = "username"
    private val passwordKey = "password"
    
    private lateinit var et_username: EditText
    private lateinit var et_password: EditText
    private lateinit var et_code: EditText

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        et_username = findViewById(R.id.et_username)
        et_password = findViewById(R.id.et_username)
        et_code = findViewById(R.id.et_code)
        
        et_username.setText(get(usernameKey))
        et_password.setText(get(passwordKey))

        
        findViewById<Button>(R.id.btn_login).apply {
            setOnClickListener(this@MainActivity)
        }
        findViewById<CodeView>(R.id.code_view).apply {
            setOnClickListener(this@MainActivity)
        }
    }
    
    override fun onClick(v: View?) {
        if (v is CodeView) {
            v.updateCode()
        } else if(v is Button) {
            login()
        }
    }

    private fun login() {
        val username = et_username.text.toString()
        val password = et_password.text.toString()
        val code = et_code.text.toString()

        val user = User(username, password, code)
        if(verify(user)) {
            save(usernameKey, username)
            save(passwordKey, password)
            startActivity(Intent(this, LessonActivity::class.java))
        }
    }

    private fun verify(user: User): Boolean {
        if (user.username.length < 4) {
            toast("用户名不合法")
            return false
        }
        if (user.password.length < 4) {
            toast("密码不合法")
            return false
        }
        return true
    }
}