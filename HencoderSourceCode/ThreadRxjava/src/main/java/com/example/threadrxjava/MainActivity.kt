package com.example.threadrxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    val url2 = "https://api.github.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl(url2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        textview.text = "正在加载"
        val api = retrofit.create(Api::class.java)

        //发起请求
        api.getRepos1()
            .enqueue(object : Callback<MutableList<Repo>?>{
                override fun onResponse(call: Call<MutableList<Repo>?>, response: Response<MutableList<Repo>?> ) {
                    textview.text = "Result: ${response.body()!![0].name}"
                }

                override fun onFailure(call: Call<MutableList<Repo>?>, t: Throwable) {
                    textview.text = "加载失败"
                }
            })
    }
}