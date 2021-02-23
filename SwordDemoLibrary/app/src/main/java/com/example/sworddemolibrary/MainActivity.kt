package com.example.sworddemolibrary

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

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

        TelephonyManagerJavaUtil.debug(this);
    }

}