package sword.okhttp.library

import java.util.concurrent.ThreadFactory


fun threadName(name: String, block: () -> Unit) {
    val oldName = Thread.currentThread().name
    Thread.currentThread().name = name
    try {
        block()
    } finally {
        Thread.currentThread().name = oldName
    }
}

fun threadFactory(name: String, daemon: Boolean) : ThreadFactory = ThreadFactory { 
    Thread(name).apply { 
        this.isDaemon =  daemon
    }
}
