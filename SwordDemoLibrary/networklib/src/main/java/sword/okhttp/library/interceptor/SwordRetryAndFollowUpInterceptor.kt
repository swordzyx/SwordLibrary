package sword.okhttp.library.interceptor

import okhttp3.internal.connection.RouteException
import okhttp3.internal.http2.ConnectionShutdownException
import okhttp3.internal.withSuppressed
import sword.okhttp.library.SwordRequest
import sword.okhttp.library.SwordResponse
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.security.cert.CertificateException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

/**
 * 1. 请求前初始化连接需要的参数；
 * 2. 请求中检测是否出现异常，然后分情况决定是否重新发送请求；
 * 3. 请求后根据状态码决定是否重新发送请求，如果不重新发送请求，则将响应报文返回
 */
class SwordRetryAndFollowUpInterceptor : SwordInterceptor {
  override fun intercept(chain: SwordInterceptorChain): SwordResponse {
    //todo: 请求
    var request = chain.request
    val call = chain.call
    var newExchangeFinder = true
    val recoverFailures = mutableListOf<IOException>()
    var followUpCount = 0
    while (true) {
      //1. 请求前：查找一个网络交互操作实例，稍后执行这个操作；这个操作实例里面会准备好执行这个操作所需要的参数
      call.enterNetworkInterceptorExchange(request, newExchangeFinder)
      
      //2. 请求中
      try {
        //todo：检查请求是否被取消了，如果取消了直接终止
        var response: SwordResponse
        try {
          response = chain.proceed(request)
        } catch (e: RouteException) { //通过某条路由线路连接失败，这种情况可以看看还有没有其他的路，然后继续尝试连接
          //再次尝试继续发送请求，先判断请求报文能不能复用，如果能复用，那就再次发送这个请求报文
          if (!tryAgain(request, false, e)) {
            throw e.firstConnectException.withSuppressed(recoverFailures)
          } else {
            recoverFailures += e.firstConnectException
          }
          newExchangeFinder = false
          continue
        } catch (e: IOException) { //跟服务器连接成功了，但是通信失败，可能是请求发送失败了，或者请求发送了，但是服务器没有返回响应
          //再次尝试继续发送请求
          if (!tryAgain(request, e !is ConnectionShutdownException, e)) {
            throw e.withSuppressed(recoverFailures)
          } else {
            recoverFailures += e
          }
          newExchangeFinder = false
          continue
        }

        //3. 请求后
        // 检查 response 的响应码，如果是 3xx，说明服务器的地址发生了迁移，从 Location 这个 Header 中拿到新的 url，获取一个指向新的 url 的请求
        val followUpRequest = when (response.code) {
          407 -> { 
            //todo:代理服务器需要认证
            request
          }

          401 -> { 
            //todo: HTTP 未授权
            request
          }

          308, 307, 300, 301, 302, 303 -> {
            //todo: 308：永久重定向
            //todo 307：临时重定向
            //todo 300：多选
            //todo 301：永久迁移
            //todo 302：临时迁移
            //todo 303：其他
            request
          }

          408 -> {
            //todo 客户端请求超时
            request
          }

          503 -> { 
            //todo 服务不可访问
            request
          }

          421 -> { 
            //todo 服务器认为这个请求应该指向其他服务器或资源；以及服务器无法确定将这个请求发往哪里
            request
          }
          else -> {
            null
          }
        } ?: return response
        
        //如果重定向的请求为 null，说明不需要进行重定向
        // todo 不需要重定向的情况下，OkHttp 针对双工通信做了一些额外的处理（例如停止超时检测）
        //双工通信指的是请求的发送和响应的接收可以在一个 HTTP 连接中同时进行，HTTP2 和 HTTP3 支持这个特性

        if (++followUpCount > 20) {
          throw ProtocolException("Too many follow-up requests: $followUpCount")
        }
        
        request = followUpRequest
      } finally {

      }
    }
  }

  //连接能够复用的条件是什么？
  //1. 网络请求发出去了，并且这是一次性请求，就不再尝试
  //2. 收到的异常时 FileNotFoundException，表示服务器那边找不到请求对应的文件，也不再尝试了。
  //3. 异常比较严重，再发送也没什么意义，就不再尝试
  //4. 已经没有可用路由了，所有的路由都尝试过，都连接（请求失败），终止尝试
  private fun tryAgain(request: SwordRequest, requestHaveSended: Boolean, e: Exception): Boolean {
    if ((requestHaveSended && request.body.isOneshot()) || e is FileNotFoundException) return false

    return !fatalException(requestHaveSended, e)
  }

  /**
   * 什么类型的异常算是致命的异常
   * 1. 协议类错误：请求报文或响应报文的格式不符合网络协议的规范
   * 2. 请求被中断：这要分情况，如果请求发送时，Socket 连接超时状态下中断了请求，那可以继续尝试；其他情况则终止尝试
   * 3. 握手失败：这种情况重试也不太可能成功，不过还是会去尝试一下，不过如果握手失败的原因是证书校验失败，直接终止请求
   * 4. SSL 认证失败：例如固定证书校验失败（有时会针对一个 url 在代码里面配置固定的证书，如果这个证书校验失败，也直接终止请求）
   */
  private fun fatalException(requestHavedSended: Boolean, e: Exception): Boolean {
    //协议发生了错误，具体表现形式是什么？
    if (e is ProtocolException) return false

    if (e is InterruptedIOException) {
      return (e is SocketTimeoutException) && !requestHavedSended
    }

    if (e is SSLHandshakeException) {
      return e.cause !is CertificateException
    }

    if (e is SSLPeerUnverifiedException) {
      return false
    }

    return true
  }
}