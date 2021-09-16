package com.example.learnkotlin.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.learnkotlin.R

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
        
        et_username.text = CacheUtils.get(usernameKey)
        et_password.text = CacheUtils.get(passwordKey)
        
        val btn_login = findViewById<Button>(R.id.btn_login).apply {
            setOnClickListener(this@MainActivity)
        }
        val img_code = findViewById<CodeView>(R.id.code_view).apply {
            setOnClickListener(this@MainActivity)
        }
        
    }
    
    override fun onClick(v: View?) {
        
    }
}