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
        
        
        //eCode 是 Button! 类型，表示这个类型不是 kotlin 声明出来的类型，而是别的平台声明出来的类型，kotlin 不能判断这个类型是否为空，因此只能用平台类型来接收它。平台类型泛指来自其他平台的类型，因为 kotlin 可以跟多种语言做交互。
        //编写代码的时候，编译器会把平台类型看成不可空类型。如果平台类型的对象为空，在使用该对象的时候依然会报空指针，可以看出使用 kotlin 并不代表永远的避免了空指针异常
        //Java 中的 @NotNull 和 @Nullable 注解可以帮助 kotlin 判断 Java 对象类型，其他语言类似
        val eCode = findViewById<EditText>(R.id.et_code)
        eCode.setText("hencoder")
        
        //delegate.findViewById 方式使用了 @Nullable 注解，表示该方法返回的对象可能为空，因此在 kotlin 中，该方法返回的对象类型就为可空类型 
        val code = delegate.findViewById<EditText>(R.id.et_code)
        code?.setText("hencoder")
        
        
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