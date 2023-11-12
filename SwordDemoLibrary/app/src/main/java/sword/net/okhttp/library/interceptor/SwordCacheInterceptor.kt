package sword.net.okhttp.library.interceptor

import sword.net.okhttp.library.SwordResponse
import sword.net.okhttp.library.cache.SwordCache
import sword.net.okhttp.library.cache.SwordCacheStrategy

class SwordCacheInterceptor: SwordInterceptor {
  private val cache = SwordCache.instance
  override fun intercept(chain: SwordInterceptorChain): SwordResponse? {
    val request = chain.request
    //todo：拿到缓存的资源
    val cachedResponse = cache.get(request)

    //todo：判断缓存的资源是否过期，通过 CacheStrategy 这个类
    val cacheStrategy = SwordCacheStrategy()
    cacheStrategy.compute(request, cachedResponse)


    //todo：如果缓存可用，则直接将缓存转为 Response 返回
    val response = chain.proceed(request)

    //todo：判断响应码是否为 304，表示资源未修改

    //todo: 更新本地缓存，具体要做的就是将这个 url 对应的缓存 Response 换成新的 Response

    return response
  }
}