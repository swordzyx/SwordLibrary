package com.example.swordlibrary.kotlin

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.loginlibrary.ListPopupWindowWithEditTextOnly
import com.example.swordlibrary.R

class MainActivity : AppCompatActivity() {
    val TAG = "zero"

    val phoneInfos = arrayOf("181****3573", "182****3573", "183****3573")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_spinner_edittext_only)

//        showSpinnerWithEditTextImageView()

        ListPopupWindowWithEditTextOnly().initView(this, R.id.popup_list_edit_only)
    }




    fun deleteToken(view: View) {
        Log.d("Sword", "delete token")
    }


}