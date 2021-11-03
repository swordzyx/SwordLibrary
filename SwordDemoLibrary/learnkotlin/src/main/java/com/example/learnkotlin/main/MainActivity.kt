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
import com.example.learnkotlin.utils.Utils.toast
import com.example.learnkotlin.utils.get
import com.example.learnkotlin.utils.save

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

        /**
         * verify() 函数确定只会再 login() 中用得到，因此可以直接将 verify 定义到 login 函数体中
         * 
         * 内部函数可以访问外部函数的成员。因此 verify(user: User) 中的 user 参数可以省略掉
         * 
         * 不过内部函数被调用的时候会生成一个额外的对象，也就是每次调用 login() 的时候都会生成一个用来调用 verify() 的对象。因此，如果 login() 会被频繁调用，就要考虑性能问题，不要老是创建出一堆垃圾对象出来
         * 内部函数调用时会创建处临时对象，因此除非定义内部函数真的能提高代码的可读性，否则不要使用
         */
        fun verify(user: User): Boolean {
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
        
        val user = User(username, password, code)
        if(verify(user)) {
            save(usernameKey, username)
            save(passwordKey, password)
            startActivity(Intent(this, LessonActivity::class.java))
        }
    }

    /**
     * 如果确定 verify 函数只会在 login 中使用到，那么可以将 verify 函数直接定义的 login() 函数中 
     */
    /*private fun verify(user: User): Boolean {
        *//**
         * if (user.username == null || user.username.length < 4) 等价于 user.username?.length ?: 0 < 4
         *
         * 说明：如果 user.username 为 null，那么user.username.length 就是 null，那么 user.username?.length ?: 0 就会返回 0
         *//*
        if (user.username.length < 4) {
            toast("用户名不合法")
            return false
        }
        if (user.password.length < 4) {
            toast("密码不合法")
            return false
        }
        return true
    }*/
}