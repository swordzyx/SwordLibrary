package sword.okhttp.library

class SwordHttpUrl(
    @get: JvmName("schema") val schema: String,
    @get: JvmName("host") val host: String,
    @get: JvmName("port") val port: Int
) {
}