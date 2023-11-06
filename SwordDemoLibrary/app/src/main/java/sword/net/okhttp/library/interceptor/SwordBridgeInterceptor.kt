package sword.net.okhttp.library.interceptor

import sword.kotlin.channelSample
import sword.net.okhttp.library.SwordResponse

class SwordBridgeInterceptor: SwordInterceptor {
  
  override fun intercept(chain: SwordInterceptorChain): SwordResponse? {
    val request = chain.request
    val response = chain.proceed(request)
    return response
    
  }
}