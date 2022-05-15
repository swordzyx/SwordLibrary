package com.httptheory.okhttp

import okhttp3.*
import java.io.IOException



class OkHttpSample {
  companion object {
    fun testRequest() {
      val url = "https://api.github.com/users/rengwuxian/repos"

      val certificatePinner = CertificatePinner.Builder()
        .add("api.github.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
        .build()

      val client = OkHttpClient.Builder()
        .certificatePinner(certificatePinner)
        .build()

      val request = Request.Builder()
        .url(url)
        .build()
      client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
          e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
          //println("response code: ${response.code}")
        }

      })
    }
  }
}