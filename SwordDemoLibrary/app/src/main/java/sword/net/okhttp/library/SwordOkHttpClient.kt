package sword.net.okhttp.library

class SwordOkHttpClient {
    fun newCall(request: Request): SwordRealCall = SwordRealCall(this, request, false)
}