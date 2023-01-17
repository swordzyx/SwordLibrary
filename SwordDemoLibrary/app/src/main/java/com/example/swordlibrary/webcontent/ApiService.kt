package com.example.swordlibrary.webcontent

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

interface ApiService {
  @GET
  fun download(@Url url: String, @HeaderMap requestHeaders: Map<String, String>): ResponseBody
}