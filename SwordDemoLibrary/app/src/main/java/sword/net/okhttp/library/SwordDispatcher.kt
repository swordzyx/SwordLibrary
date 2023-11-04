package sword.net.okhttp.library

import okhttp3.internal.connection.RealCall
import java.lang.IllegalArgumentException

class SwordDispatcher {
    private val readyCallQueue = ArrayDeque<SwordRealCall.AsyncCall>()

    //最大请求数
    @get:Synchronized
    var maxRequests = 64
        set(value) {
            if (value < 1) throw IllegalArgumentException("max < 1, $value")
            synchronized(this) {
                field = value
            }
        }

    //能够发往同一个主机的最大请求数
    @get:Synchronized
    var maxRequestPreHost = 5
        set(value) {
            if (value < 1) throw IllegalArgumentException("maxRequestPreHost < 1, $value")
            field = value
        }

    private val runningCallDeque = ArrayDeque<SwordRealCall.AsyncCall>()
    internal fun enqueue(call: SwordRealCall.AsyncCall): Boolean {
        val excutableCall = mutableListOf<SwordRealCall.AsyncCall>()
        var isRunning = false
        synchronized(this) {
            //将 call 添加到一个队列里面（先进先出）
            readyCallQueue.add(call)

            //查找是否有一个可以重用的 Call，是如何判断一个 Call 是否可以被重用的，只有 HTTP 连接才可以重用，WebSocket 连接是无法重用的

            //遍历队列中所有的 Call
            val i = readyCallQueue.iterator()
            while (i.hasNext()) {
                val asyncCall = i.next()
                //判断请求数有没有超，使用一个队列保存正在运行的 Call，判断这个队列的数量是否达到最大值
                if (runningCallDeque.size > maxRequests) break
                if (asyncCall.callsPerHost.get() >= maxRequestPreHost) continue
                i.remove()

                //没有超的话就把这个 Call 移到一个数组中待之后运行
                asyncCall.callsPerHost.getAndIncrement()
                excutableCall.add(call)
                runningCallDeque.add(call)
            }
            isRunning = runningCallDeque.size > 0
        }
        return isRunning
    }
}