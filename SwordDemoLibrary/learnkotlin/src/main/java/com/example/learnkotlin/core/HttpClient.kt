package com.example.learnkotlin.core

import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type

private val gson = Gson()

private fun <T> convert(json: String, type: Type): T {
    return gson.fromJson(json, type)
}

object HttpClient : OkHttpClient() {

    fun <T> get(path: String, type: Type, entityCallback: EntityCallback<T>) {
        val request = Request.Builder()
            .url("https://api.hencoder.com/${path}")
            .build()
        val call = newCall(request)

        //使用 object 关键字创建匿名内部类
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                entityCallback.onFailure("网络异常")
            }

            override fun onResponse(call: Call, response: Response) {
                when(response.code) {
                    in 200..299 -> {
                        val body = response.body
                        var json: String? = null
                        try {
                            json = body!!.string()
                        } catch (e: IOException) {
                            //在 java 中是必须捕获这个 IO 异常的。在 kotlin 中，如果知道这个异常是绝对不会抛出的，其实是可以将这个地方的 try-catch 代码块删掉
                            e.printStackTrace()
                        }
                        entityCallback.onSuccess(convert(json!!, type))
                    }
                    in 400..499 -> {
                        entityCallback.onFailure("客户端错误")
                    }
                    in 500..599 -> {
                        entityCallback.onFailure("服务器错误")
                    }
                    else -> {
                        entityCallback.onFailure("未知错误")
                    }
                }

            }

        })
    }

}