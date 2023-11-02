package sword.net.okhttp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.http.RealInterceptorChain
import java.io.IOException

fun main() = runBlocking {
    val githubService = GithubService()

    githubService.listRepos("swordzyx")
}

class GithubService {
    private val baseUrl = "https://api.github.com"
    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor {
            val response = (it as RealInterceptorChain).proceed(it.request())
            println("body: ${response.body?.string()}, response: $response")
            response
        }
        .addInterceptor { interceptorChain ->
            val response = (interceptorChain as RealInterceptorChain).proceed(interceptorChain.request())
            println("body: ${response.body?.string()}, response: $response")
            response
        }
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