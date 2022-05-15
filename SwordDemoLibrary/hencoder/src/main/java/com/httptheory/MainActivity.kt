package com.sword.httptheory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.httptheory.okhttp.OkHttpSample

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //RetrofitNetUtil().requestByRetrofit()
        OkHttpSample.testRequest()
    }
}