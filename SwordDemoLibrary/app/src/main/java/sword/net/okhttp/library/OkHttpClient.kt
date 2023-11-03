package sword.net.okhttp.library

class OkHttpClient {
    fun newCall(request: Request): RealCall = RealCall(this, request, false)
}