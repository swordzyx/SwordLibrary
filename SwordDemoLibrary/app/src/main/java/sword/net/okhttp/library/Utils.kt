package sword.net.okhttp.library

fun threadName(name: String, block: () -> Unit) {
    val oldName = Thread.currentThread().name
    Thread.currentThread().name = name
    try {
        block()
    } finally {
        Thread.currentThread().name = oldName
    }
}