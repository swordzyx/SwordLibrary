package sword.net.okhttp.library.interceptor

import sword.net.okhttp.library.SwordRealCall
import sword.net.okhttp.library.SwordRequest
import sword.net.okhttp.library.SwordResponse

fun interface SwordInterceptor {
  fun intercept(chain: SwordInterceptorChain): SwordResponse?
}

class SwordInterceptorChain(
  val call: SwordRealCall,
  private val interceptors: List<SwordInterceptor>,
  val index: Int,
  val request: SwordRequest 
) {
  
  fun copy(index: Int, request: SwordRequest): SwordInterceptorChain = 
    SwordInterceptorChain(call, interceptors, index, request)

  /**
   * 将 request 传给下一个 Interceptor
   */
  fun proceed(request: SwordRequest): SwordResponse {
    
    //应该是获取到下一个 Interceptor，也就是第 index + 1 个 Interceptor
    //将 Request 传递到下一个 Interceptor 这个行为是在 Interceptor 内部进行的，就是当前 Interceptor 的 intercept 方法针对请求的处理完成之后，将请求传给下一个 Interceptor
    //这意味着 Interceptor 内部需要保存 InterceptorChain 这个对象，然后有个索引，指向接下来请求要达到的 Interceptor
    //todo:不过为何要创建一个新的 InterceptorChain，而不是直接将当前 InterceptorChain 的 index 加 1，传入到 Interceptor 中，这会不会跟设计模式有关，就是 OkHttp 的设计者要求 InterceptorChain 这个对象的内部状态应该是不可更改的？
    val next = copy(index + 1, request)
    val interceptor = interceptors[index]
    
    // 然后执行这个 Interceptor 的 intercept 方法
    val response =  interceptor.intercept(next) ?: throw NullPointerException("interceptor $interceptor return null")

    //检查响应报文的 body 是否为 null，如果为 null，则抛异常
    check(response.body != null) {
      "interceptor $interceptor return response with no body"
    }
    return response
  }
}