package sword.net.okhttp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.http.RealInterceptorChain
import sword.SwordLog
import sword.net.okhttp.library.string
import java.io.IOException

fun main() = runBlocking {
    val githubService = GithubService()

    githubService.listRepos("swordzyx")
}

class GithubService {
    private val tag = "GithubService"
    private val baseUrl = "https://api.github.com"
    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor { interceptor ->
            val request = interceptor.request()
            println("interceptor 拦截：$request")
            val response = interceptor.proceed(interceptor.request())
            println("body: ${response.body?.string()}, response: $response")
            response
        }
        .addInterceptor { interceptorChain ->
            val request = interceptorChain.request()
            println("interceptor 拦截：$request")
            val response = (interceptorChain as RealInterceptorChain).proceed(request)
            println("body: ${response.body?.string()}, response: $response")
            response
        }
        .eventListener(object : EventListener() {
            override fun callEnd(call: Call) {
                SwordLog.debug(tag, "call end: ${call.string()}")
            }

            override fun callFailed(call: Call, ioe: IOException) {
                SwordLog.debug(tag, "call failed: ${call.string()}, exception: " + ioe.toString())
            }

            override fun callStart(call: Call) {
                SwordLog.debug(tag, "call start: ${call.string()}")
            }
        })
        .build()

    private val scope = CoroutineScope(Dispatchers.Default)

    fun listRepos(user: String) {
        val request = Request.Builder()
            .url("$baseUrl/users/$user/repos")
            .build()
        try {
            okHttpClient.newCall(request).execute().apply {
                println("response: ${body?.string()}")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}