package com.sword.httptheory.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetUtil {
    private val baseUrl = "https://api.github.com/"

    fun requestByRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)
        val request = service.listRepos("octocat")

        request.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                println("Response: ${response.body()!![0].name}")
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                println("request failed")
            }

        })
    }
}