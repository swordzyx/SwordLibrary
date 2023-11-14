package sword.kotlin.kthttp

import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import kotlin.coroutines.resumeWithException

fun main() = sample() /*{
  SwordRetrofit
    .apply { baseUrl = "https://baseurl.com" }
    .create<GithubService>()
    .reposV2("Kotlin", "weekly").call(object : Callback<RepoList> {
      override fun onSuccess(data: RepoList) {
        println("repoList: $data")
      }

      override fun onFailed(throwable: Throwable) {
        throwable.printStackTrace()
        println("request failed, $throwable")
      }

    })
}*/

fun sample() = runBlocking {
  val start = System.currentTimeMillis()
  //Async底层使用的线程池没有立即回收，代码执行完成后要等一会才会回收
  val deferred = async {
    SwordRetrofit.apply {
      baseUrl =  "https://baseurl.com"
    }.create<GithubService>().reposV2("Kotlin", "weekly").await()
  }
  
  deferred.invokeOnCompletion { 
    println("invokeOnCompletion, ${System.currentTimeMillis() - start}")
  }
  
  delay(10)
  deferred.cancel()
  println("Time cancel: ${System.currentTimeMillis() - start}")
  
  try {
    println("await")
    println(deferred.await())
  } catch (e: Exception) {
    println("Time exception: ${System.currentTimeMillis() - start}")
    println("Catch exception: $e, Thread: ${Thread.currentThread().name}")
  } finally {
    println("Time Total: ${System.currentTimeMillis() - start}, Thread: ${Thread.currentThread().name}")
  }
}

interface Callback<T : Any> {
  fun onSuccess(data: T)
  fun onFailed(throwable: Throwable)
}

class KtCall<T : Any>(
  private val call: Call,
  private val gson: Gson,
  private val type: Type
) {
  fun call(callback: Callback<T>): Call {
    try {
      println("enqueue request")
      call.enqueue(object : okhttp3.Callback {
        override fun onFailure(call: Call, e: IOException) {
          println("okhttp callback onFailure, Thread:  ${Thread.currentThread().name}")
          callback.onFailed(e)
        }

        override fun onResponse(call: Call, response: Response) {
          println("okhttp callback onResponse, Thread:  ${Thread.currentThread().name}")
          try {
            val data = gson.fromJson<T>(response.body?.string(), type)
            callback.onSuccess(data)
          } catch (e: Exception) {
            callback.onFailed(e)
          }
        }
      })
    } catch (e: Exception) {
      println("catch enqueue Execption: $e, Thread: ${Thread.currentThread().name}")
    }
    return call
  }

  suspend fun await(): T = suspendCancellableCoroutine { continuation ->
    val call = call(object : Callback<T> {
      override fun onSuccess(data: T) {
        try {
          println("request Success, Thread: ${Thread.currentThread().name}")
          continuation.resumeWith(Result.success(data))
        } catch (e: Exception) {
          println("exception on resume: $e, Thread: ${Thread.currentThread().name}")
        }
      }

      override fun onFailed(throwable: Throwable) {
        try {
          continuation.resumeWithException(throwable)
        } catch (e: Exception) {
          println("exception on resumeWithException: $e, Thread: ${Thread.currentThread().name}")
        }
      }
    })

    /*continuation.invokeOnCancellation {
      call.cancel()
    }*/
  }
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

  inline fun <reified T : Any> create(): T {
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
        ?.mapIndexed { index, annotations -> Pair(annotations, args[index]) }
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
        ?.let {
          if ((method.genericReturnType as ParameterizedType).rawType == KtCall::class.java) {
            KtCall<T>(okHttpClient.newCall(it), gson, method.genericReturnType)
          } else {
            gson.fromJson(
              okHttpClient.newCall(it).execute().body?.string(),
              method.genericReturnType
            )
          }
        }
    } as T
  }

  private fun getTypeArgument(method: Method) {
    (method.genericReturnType as ParameterizedType).let { parameterizedType ->
      parameterizedType.actualTypeArguments.forEach {
        println("actualType: $it")
      }
      parameterizedType.actualTypeArguments[0]
    }
  }
}