package com.sword.floatball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.utilclass.LogUtil
import java.lang.StringBuilder

class WebViewActivity : AppCompatActivity() {
	private val url = "http://192.168.18.86:8080/feedback"
	private val params = hashMapOf(
		"role_name" to "",
		"role_id" to "",
		"server_id" to "",
		"vip" to "",
		"channel_id" to "",
		"device" to "",
		"ip" to "",
		"sign" to "",
		"os" to "",
		"apkVersionDesc" to "",
		"resVersion" to "",
		"osVersion" to ""
	)
	private lateinit var webView: WebView


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_web_view)

		webView = findViewById(R.id.webview)
		webView.loadUrl(buildUrl())
	}

	private fun buildUrl(): String {
		val sb = StringBuilder("$url?")
		for ((index, entry) in params.entries.withIndex()) {
			sb.append("${entry.key}=${entry.value}")
			if (index < params.size - 1) {
				sb.append("&")
			}
		}
		LogUtil.debug("buildUrl: $sb")
		return sb.toString()
	}
}