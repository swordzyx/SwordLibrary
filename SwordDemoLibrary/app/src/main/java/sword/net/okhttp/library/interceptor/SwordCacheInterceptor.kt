package sword.net.okhttp.library.interceptor

import sword.net.okhttp.library.SwordResponse

class SwordCacheInterceptor: SwordInterceptor {
  override fun intercept(chain: SwordInterceptorChain): SwordResponse? {
    //todo：拿到缓存的资源

    //todo：判断缓存的资源是否过期
    //1. 通过 Expire 这个 Header 判断
    //2. 用当前时间减去 Date 这个，与 Cache-Control 中的 max-age 比较
    //3. 发送一个请求，带上 If-None-Match，值为缓存的资源的 Etag 值
    //4. 发送一个请求，带上 If-NoModified-Since，它的值为缓存的资源的 Last-Modified 的值

    //todo：如果缓存可用，则直接将缓存转为 Response 返回
    
  }
}