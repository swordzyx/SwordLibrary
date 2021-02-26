package com.example.sworddemolibrary.kotlin

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sworddemolibrary.R

class MainActivity : AppCompatActivity() {
    val TAG = "zero"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //倒计时器
//        CountDownTimerDemo.start()
        //TelephonyManager 信息打印
//        TelephonyManagerUtil.debug(this)
    }

}