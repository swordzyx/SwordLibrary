package com.sword.webbasecontent

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import com.example.utilclass.LogUtil

class MainActivity : AppCompatActivity() {
	lateinit var webView: WebView
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		webView = findViewById(R.id.webView)
		/**
		 * 1. Android 通过 loadUrl 调用 JS 代码
		 */
		//configureWebView()

		/**
		 * 2. 通过 WebView 的 evaluateJavascript 执行 JS 代码，这种方法更高效，因为它不会导致 url 刷新，而 loadUrl 会导致页面刷新
		 */
		callJSByEvaluateJavascript() 
		
	}

	/**
	 * 仅 Android 4.4 及以上的设备可以执行 WebView#evaluateJavascript
	 */
	private fun callJSByEvaluateJavascript() {
		webView.evaluateJavascript("javascript:callJs()") {
			LogUtil.debug("js result: $it")
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private fun configureWebView() {
		//设置允许在 WebView 中执行 JS 代码
		webView.settings.javaScriptEnabled = true
		//允许 JS 弹框
		webView.settings.javaScriptCanOpenWindowsAutomatically = true
		//允许使用 file 协议，也就是允许 WebView 加载 file:// 开头的 url
		webView.settings.allowFileAccess = true
		
		webView.webChromeClient = webChromeClient
		webView.webViewClient = webViewClient
		
		//加载 assets 中的文件固定格式为：file:///android_assets/文件名.html，这个 url 的 schemas（协议）是 file
		webView.loadUrl("file:///android_asset/javascript.html")
	}
	
	private val webViewClient = object : WebViewClient() {
		/**
		 * js 的代码必须在 onPageFinished 之后调用才有效
		 */
		override fun onPageFinished(view: WebView?, url: String?) {
			super.onPageFinished(view, url)
			//执行 javascript.html 中的 callJs() 方法。
			webView.loadUrl("javascript:callJs()")
		}
	}
	
	private val webChromeClient = object : WebChromeClient() {
		/**
		 * Js 执行 alert() 弹框时回调，让主应用程序决定是否拦截。如果返回 false 表示不拦截，JS 继续弹框，如果返回 true，则拦截，弹自定义 Dialog，不过在 Dialog 关闭之后需要执行 JsResult#confirm() 方法。
		 */
		override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
			AlertDialog.Builder(this@MainActivity)
				.setTitle("Android 执行 JS")
				.setMessage(message)
				.setPositiveButton("确定") { _, _ ->
					result?.confirm()
				}
				.setCancelable(true)
				.create().show()
			return true
		}
	}
}