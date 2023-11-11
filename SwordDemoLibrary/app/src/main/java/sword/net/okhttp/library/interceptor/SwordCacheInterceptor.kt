package sword.net.okhttp.library.interceptor

import sword.net.okhttp.library.SwordResponse

class SwordCacheInterceptor: SwordInterceptor {
  override fun intercept(chain: SwordInterceptorChain): SwordResponse? {
    //todo：拿到缓存的资源

    //todo：判断缓存的资源是否过期

    //todo：如果缓存可用，则直接将缓存转为 Response 返回
    
  }
}