package com.example.swordlibrary.kotlin

import android.content.Context
import android.database.DataSetObserver
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.R

class MainActivity : AppCompatActivity() {
    val TAG = "zero"

    val phoneInfos = arrayOf("181****3573", "182****3573", "183****3573")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_spinner_edittext_only)

//        showSpinnerWithEditTextImageView()

        ListPopupWindowWithEditTextOnly().createView(this)
    }




    fun deleteToken(view: View) {
        Log.d("Sword", "delete token")
    }


}