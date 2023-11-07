package sword.net.okhttp.library.interceptor

import sword.net.okhttp.library.SwordResponse

/**
 * 拷贝一份原始请求报文，然后完善请求报文副本的 Header
 * 拷贝一份原始响应报文，解压响应报文中的 body，移除响应报文中 Content-Length 和 Content-Type 这两个 Header
 */
class SwordBridgeInterceptor: SwordInterceptor {
  
  override fun intercept(chain: SwordInterceptorChain): SwordResponse? {
    val request = chain.request
    //todo：填充 Content-Type Header
    //todo：填充 Content-Length 和 Transfer-Encoding 这两个 Header
    //todo：填充 Host Header
    //todo：填充 Connection Header
    //todo：如果请求报文中没有 Accept-Encoding 这个 Header，则填充 Accept-Encoding Header
    //todo：填充 Cookie Header
    //todo：填充 User-Agent Header
    val response = chain.proceed(request)
    //todo：保存新的 Cookie
    //todo：如果 Accept-Encoding 这个 Header 是 OkHttp 加的，解压响应报文的 Body，使用的是 Okio 中的 GzipSource
    //todo：移除 Content-Encoding Header
    //todo：移除 Content-Length Header
    return response
    
  }
}