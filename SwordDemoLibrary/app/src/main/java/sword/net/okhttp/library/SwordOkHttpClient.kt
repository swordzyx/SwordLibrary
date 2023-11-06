package sword.net.okhttp.library

import sword.net.okhttp.library.interceptor.SwordInterceptor

class SwordOkHttpClient {
    internal var dispatcher = SwordDispatcher()
    internal var eventListenerFactory = object : SwordEventListener() {
        
    }.asFactory()
    internal var interceptors = mutableListOf<SwordInterceptor>()
    internal var networkInterceptors = mutableListOf<SwordInterceptor>()
    internal var connectionPool: SwordConnectionPool = SwordConnectionPool()
    fun newCall(request: SwordRequest): SwordRealCall = SwordRealCall(this, request, false)
    
    fun dispatcher(dispatcher: SwordDispatcher) {
        this.dispatcher = dispatcher
    }
    
    fun eventListener(eventListener: SwordEventListener) {
        this.eventListenerFactory = eventListener.asFactory()
    }

    fun interceptor(interceptor: SwordInterceptor) {
        interceptors += interceptor
    }
    
    fun networkInterceptor(networkInterceptor: SwordInterceptor) {
        networkInterceptors += networkInterceptor
    }
}