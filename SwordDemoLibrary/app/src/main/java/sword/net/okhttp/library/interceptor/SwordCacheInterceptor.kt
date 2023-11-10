package sword.net.okhttp.library.interceptor

import sword.net.okhttp.library.SwordResponse

class SwordCacheInterceptor: SwordInterceptor {
  override fun intercept(chain: SwordInterceptorChain): SwordResponse? {
    //查看缓存中是否有可用的内容，有就直接返回
    
  }
}