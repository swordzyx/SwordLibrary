package sword.net.okhttp.library.interceptor

import sword.net.okhttp.library.SwordResponse
import sword.net.okhttp.library.cache.SwordCache

class SwordCacheInterceptor: SwordInterceptor {
  private val cache = SwordCache.instance
  override fun intercept(chain: SwordInterceptorChain): SwordResponse? {
    val request = chain.request
    //todo：拿到缓存的资源
    val cacheResponse = cache.get(request)

    //todo：判断缓存的资源是否过期

    //todo：如果缓存可用，则直接将缓存转为 Response 返回
    chain.proceed(request)
  }
}