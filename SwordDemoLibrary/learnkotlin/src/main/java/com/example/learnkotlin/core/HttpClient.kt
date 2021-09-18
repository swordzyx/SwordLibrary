package com.example.learnkotlin.core

import com.google.gson.Gson
import okhttp3.OkHttpClient

class HttpClient : OkHttpClient() {
    val INSTANCE = HttpClient()
    private val gson = Gson()

}