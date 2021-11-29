package com.sword.httptheory.retrofit

import com.example.utilclass.LogUtil
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.function.Consumer

const val tag = "requestRxjava -"

class RetrofitNetUtil {
	private val baseUrl = "https://api.github.com/"

	fun requestByRetrofit() {
		val retrofit = Retrofit.Builder()
			.baseUrl(baseUrl)
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.build()

		val service = retrofit.create(GitHubService::class.java)
		val request = service.listRepos("octocat")

		val requestRxjava = service.listReposRxjava("octocat")
		requestRxjava.subscribe(object : SingleObserver<List<Repo>> {
			override fun onSubscribe(d: Disposable?) {
				LogUtil.debug("$tag onSubscribe")
			}

			override fun onSuccess(value: List<Repo>) {
				LogUtil.debug("requestRxjava - success: ${value[0].name}")
			}

			override fun onError(e: Throwable?) {
				LogUtil.debug("$tag requestRxjava error")
			}

		})

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