package com.example.swordlibrary.webcontent.core

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.swordlibrary.webcontent.ApiService
import com.example.swordlibrary.webcontent.getWebViewCachePath
import com.sword.LogUtil
import kotlinx.coroutines.runBlocking
import okio.ByteString.Companion.encodeUtf8
import retrofit2.Retrofit
import java.io.File

/**
 * 缓存管理
 */
class CustomWebviewClient : WebViewClient() {
  private val TAG = "SwordWebviewClient"
  private val fileApiService by lazy { 
    Retrofit.Builder().build().create(ApiService::class.java)
  }

  override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
    super.onPageStarted(view, url, favicon)
    //开启白屏检测
    if(view is BaseWebView) {
      view.postBlankMonitorRunnable()
    }
  }

  override fun onPageFinished(view: WebView?, url: String?) {
    super.onPageFinished(view, url)
    //页面加载完成之后取消白屏检测
    if(view is BaseWebView) {
      view.cancelBlankMonitorRunnable()
    }
  }
  

  /**
   * 证书校验错误
   */
  override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
    LogUtil.debug(TAG, "onReceivedSslError")
    AlertDialog.Builder(view.context)
      .setTitle("提示")
      .setMessage("当前网站安全证书已过期或不可信\n是否继续浏览")
      .setPositiveButton("继续浏览") { dialog, which ->
        dialog.dismiss()
        handler.proceed()
      }
      .setPositiveButton("返回上一页") { dialog, which ->
        dialog.dismiss()
        handler.cancel()
      }
      .show()
  }

  /**
   * 资源加载失败（可能是无法访问到服务器）
   * 已被弃用的那版 onReceivedError 仅在主页加载失败的时候会回调，而新版 onReceivedError 则会再任何资源加载失败时都会被回调
   * 可以在此回调中执行一些必需的请求操作
   */
  override fun onReceivedError(
    view: WebView,
    request: WebResourceRequest,
    error: WebResourceError
  ) {
    LogUtil.debug(TAG, "onReceivedError, WebResourceRequest: ${request.logString()}")
    if (request.isForMainFrame) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        onReceivedError(view, error.errorCode, error.description.toString(), request.url.toString())
      }
    }
  }

  @Deprecated(
    "Deprecated in Java", ReplaceWith(
      "super.onReceivedError(view, errorCode, description, failingUrl)",
      "android.webkit.WebViewClient"
    )
  )
  override fun onReceivedError(
    view: WebView,
    errorCode: Int,
    description: String,
    failingUrl: String
  ) {
    LogUtil.debug(
      TAG,
      "onReceivedError, errorCode: $errorCode, description: $description, failingUrl: $failingUrl"
    )
    super.onReceivedError(view, errorCode, description, failingUrl)
  }

  /**
   * WebView 加载 url 之前回调；可在此处拦截 WebView 加载的 url（非 post 方法）；
   * 新版的 shouldOverrideUrlLoading 在 Webview
   * 返回 true ，Webview 会停止加载 url；
   * 返回 false，Webview 会继续加载 url；
   */
  override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
    LogUtil.debug(TAG, "shouldOverrideUrlLoading, \n\n WebResourceRequest: ${request.logString()}")

    return shouldOverrideUrlLoading(view, request.url.toString())
  }

  @Deprecated(
    "Deprecated in Java", ReplaceWith(
      "super.shouldOverrideUrlLoading(view, url)",
      "android.webkit.WebViewClient"
    )
  )
  override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
    LogUtil.debug(TAG, "shouldOverrideUrlLoading, url: $url")
    val schema = Uri.parse(url).scheme ?: return false
    when (schema) {
      "http", "https" -> view.loadUrl(url)
      //其他协议
    }
    return true
  }

  /**
   * webview 发起一个资源请求（http://，https://，data://，file://，...），主应用程序是否拦截，
   * 如果返回的 WebResourceResponse 为 null，则 Webview 会继续请求资源，如果不为 null，Webview 会使用此方法返回的 WebResourceResponse 中的资源
   *
   * 加载 javascript:URL，blob:URL，file:///android_asset/ URL，file:///android_res/ URL 时，此方法不会被回调
   */
  override fun shouldInterceptRequest(
    view: WebView,
    request: WebResourceRequest
  ): WebResourceResponse? {
    LogUtil.debug(TAG, "shouldInterceptRequest >> \n ${request.logString()}")
    var webResourceResponse: WebResourceResponse? = null
    val urlString = request.url.toString()
    if (isAssetsResource(urlString)) {
      webResourceResponse = assetsResourceRequest(view.context, urlString)
    }

    if (isCacheResource(urlString)) {
      webResourceResponse = cacheResourceRequest(view.context, request)
    }

    return webResourceResponse
  }

  private fun cacheResourceRequest(
    context: Context,
    webRequest: WebResourceRequest
  ): WebResourceResponse? {
    val url = webRequest.url.toString()
    val mimeType = getMimeTypeFromUrl(url)
    if (isImageResource(url)) {
      return try {
        val file = Glide.with(context).download(url).submit().get()
        val webResourceResponse = WebResourceResponse(mimeType, "UTF-8", file.inputStream())
        webResourceResponse.responseHeaders = mapOf("access-control-allow-origin" to "*")
        webResourceResponse
      } catch (e: Exception) {
        e.printStackTrace()
        null
      }
    }

    val webCachePath = getWebViewCachePath(context) 
    val cacheFile = File(
      "$webCachePath${File.separator}success-${
        url.encodeUtf8().md5().hex()
      }"
    )
    if (!cacheFile.exists() || !cacheFile.isFile) {
      val sourceFile = File("$webCachePath${File.separator}${url.encodeUtf8().md5().hex()}")
      
      runBlocking { 
        try {
          fileApiService.download(url, webRequest.requestHeaders).use {
            it.byteStream().use { inputStream ->
              sourceFile.writeBytes(inputStream.readBytes())
            }
          }
          sourceFile.renameTo(cacheFile)
        } catch (e: Exception) {
          e.printStackTrace()
          sourceFile.deleteOnExit()
          cacheFile.deleteOnExit()
        }
      }
    }
    
    if (cacheFile.exists() && cacheFile.isFile) {
      val webResourceResponse = WebResourceResponse(mimeType, "UTF-8", cacheFile.inputStream())
      webResourceResponse.responseHeaders = mapOf("access-control-allow-origin" to "*")
      return webResourceResponse
    }
    return null
  }

  private fun isImageResource(url: String): Boolean {
    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    return extension == "ico" || extension == "bmp" || extension == "gif"
        || extension == "jpeg" || extension == "jpg" || extension == "png"
        || extension == "svg" || extension == "webp"
  }

  private fun assetsResourceRequest(context: Context, url: String): WebResourceResponse {
    val fileName = url.substring(url.lastIndexOf("/") + 1)
    val suffix = url.substring(url.lastIndexOf(".") + 1)
    val webResourceRequest = WebResourceResponse(
      getMimeTypeFromUrl(url),
      "UTF-8",
      context.assets.open("$suffix/$fileName")
    ).apply {
      responseHeaders = mapOf("access-allow-control-origin" to "*")
    }
    return webResourceRequest
  }

  private fun getMimeTypeFromUrl(url: String): String {
    try {
      val extension = MimeTypeMap.getFileExtensionFromUrl(url)
      if (extension.isBlank() || extension == "null") {
        if (extension == "json") {
          return "application/json"
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return "*/*"
  }

  private fun isCacheResource(url: String): Boolean {
    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    return extension == "ico" || extension == "bmp" || extension == "gif" || extension == "jepg"
        || extension == "jpg" || extension == "png" || extension == "svg" || extension == "webp"
        || extension == "css" || extension == "js" || extension == "json" || extension == "eot"
        || extension == "otf" || extension == "ttf" || extension == "woff"
  }

  private fun isAssetsResource(url: String): Boolean {
    return url.startsWith("file:///android_asset/")
  }

  private fun WebResourceRequest.logString(): String {
    var log = " url: $url, " +
        "isForMainFrame: $isForMainFrame, " +
        "requestHeaders: $requestHeaders, " +
        "requestMethod: $method"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      log += "isRedirect: $isRedirect"
    }
    return log
  }
}

