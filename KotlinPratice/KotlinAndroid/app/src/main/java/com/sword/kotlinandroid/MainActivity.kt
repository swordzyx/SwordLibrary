package com.sword.kotlinandroid

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.sword.base.utils.CacheUtils

class MainActivity: AppCompatActivity(), View.OnClickListener {
    private val usernameKey: String = "username"
    private val passwordKey: String = "password"

    private lateinit var et_username: EditText
    private lateinit var et_password: EditText
    private lateinit var et_code: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_code = findViewById(R.id.et_code);

        et_username.setText(CacheUtils.get(usernameKey));
        et_password.setText(CacheUtils.get(passwordKey));


    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}