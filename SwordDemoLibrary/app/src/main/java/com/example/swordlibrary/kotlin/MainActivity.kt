package com.example.swordlibrary.kotlin

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.R

class MainActivity : AppCompatActivity() {
    val TAG = "zero"

    val phoneInfos = arrayOf("181****3573", "182****3573", "183****3573")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Spinner 控件示例
        setContentView(R.layout.spinner_sample_layout)
        SpinnerSample(this).spinnerSample()
    }

    //EditText 实现编辑框下拉
    private fun SpinnerWithEditTextOnlu() {
        //编辑框下拉
        //setContentView(R.layout.edittext_with_spinner)
        //编辑框下拉
        // ListPopupWindowWithEditTextOnly().initView(this, R.id.popup_list_edit_only)
    }


    fun deleteToken(view: View) {
        Log.d("Sword", "delete token")
    }


}