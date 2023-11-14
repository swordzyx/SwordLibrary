package sword.okhttp.library.cache

import sword.okhttp.library.SwordRequest
import sword.okhttp.library.SwordResponse

class SwordCacheStrategy {
  val networkRequest: SwordRequest?
    get() = computeRequest
  val cachedResponse: SwordResponse?
    get() = computeCachedResponse
  
  private var computeRequest: SwordRequest? = null
  private var computeCachedResponse: SwordResponse? = null 
  fun compute(request: SwordRequest, cachedResponse: SwordResponse?) {
    //本地判断缓存是否可用
    //todo：获取当前时间
    val currentTime = System.currentTimeMillis()
    
    //todo：获取 Expired 这个 Header，与当前时间比较
    //todo：如果当前时间大于 Expired，缓存过期，要重新获取
    //todo：如果当前时间小于 Expired，直接使用缓存
    
    //todo：将当前时间减去 Date 这个 header 的值得到缓存的 age，与 Cache-Control 这个 Header 中的 max-age 比较
    //todo：如果大于 max-age，当前缓存过期，需要重新请求资源
    //todo：如果小于 max-age，本地缓存可用，直接使用本地缓存
    
    //发送网络请求询问缓存是否可用、
    //todo：如果 cachedResponse 中有 Etag 这个 Header，往 request 中增加 If-None-Match，附上 cachedResponse 中的 Etag header
    //todo：如果 cachedResponse 中有 Last-Modified 这个 Header，在 request 中增加 If-NoModified-Since 这个 Header，附上 Last-Modified 这个 Header 的值
  }
}