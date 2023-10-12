package sword.kotlin.kthttp

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import sword.SwordLog
import java.lang.reflect.Proxy

fun main() {
  val githubService = SwordRetrofit
    .apply { baseUrl = "https://baseurl.com" }
    .create<GithubService>()
  val repoList = githubService.repos("Kotlin", "weekly")
  SwordLog.debug(SwordRetrofit.tag, "repoList: $repoList")
}

object SwordRetrofit {
  const val tag = "SwordRetrofit"
  //底层用 OkHttp
  val okHttpClient by lazy {
    OkHttpClient()
  }
  val gson by lazy {
    Gson()
  }
  var baseUrl = "https://api.github.com"
  
  inline fun <reified T> create(): T {
    return Proxy.newProxyInstance(
      T::class.java.classLoader,
      arrayOf<Class<*>>(T::class.java)
    ) { _, method, args ->

      //获取 GET 注解的值
      var requestUrl = baseUrl
      method.annotations.forEach { annotation ->  
        if (annotation is GET) {
          requestUrl += annotation.value
        }
      }
      
      method.annotations.filterIsInstance<GET>()
        .takeIf { it.size == 1 }
        ?.let { requestUrl + it[0].value }
      
      //获取函数参数的注解，拼接成 url
      return@newProxyInstance method.parameterAnnotations
        .takeIf { it.size == args.size }
        ?.mapIndexed{ index, annotations -> Pair(annotations, args[index]) }
        ?.fold(requestUrl) { url, pair ->
          pair.first.filterIsInstance<Field>().forEach { field ->
            requestUrl += if (!url.contains("?")) {
              "?${field.value}=${pair.second}"
            } else {
              "&${field.value}=${pair.second}"
            }
          }
          println("请求 url: $requestUrl")
          return@fold requestUrl
        }
        ?.let { Request.Builder().url(it).build() }
        ?.let { okHttpClient.newCall(it).execute().body?.string() }
        ?.let { gson.fromJson<Any>(it, method.genericReturnType) }
    } as T
  }
}