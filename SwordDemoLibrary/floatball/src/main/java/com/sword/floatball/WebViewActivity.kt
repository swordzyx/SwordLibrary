package com.sword.floatball

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.utilclass.LogUtil
import java.lang.Exception
import java.lang.StringBuilder
import java.net.Inet4Address
import java.net.NetworkInterface

class WebViewActivity : AppCompatActivity() {
	private val url = "http://192.168.18.86:8080/feedback"
	private val params = hashMapOf(
		"role_name" to "宇文苑帆", //XLCWGameConfig.getRoleInfo().get("role_name")
		"role_id" to "112519885552655239", //XLCWGameConfig.getRoleInfo().get("role_id")
		"server_id" to "31", //XLCWGameConfig.getRoleInfo().get("server_id")
		"vip" to "", //XLCWGameConfig.getRoleInfo().get("vip")
		"channel_id" to "", //XLCWGameConfig.getChannelId()
		"device" to "${Build.MODEL} / ${Build.BRAND}", //设备型号 SystemUtil.getSystemModel()
		"ip" to "${getIpv4Address()}",
		"os" to "0", //系统类型，固定为 Android，XLCWGameConfig.getOS()
		"apkVersionDesc" to BuildConfig.VERSION_NAME,
		"resVersion" to "", //游戏资源版本号
		"osVersion" to Build.VERSION.RELEASE
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

	private fun getIpv4Address(): String? {
		try {
			val en = NetworkInterface.getNetworkInterfaces()
			while (en.hasMoreElements()) {
				val intf = en.nextElement()
				val ipAddr = intf.inetAddresses
				while (ipAddr.hasMoreElements()) {
					val inetAddress = ipAddr.nextElement()
					// ipv4地址
					if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
						return inetAddress.getHostAddress()
					}
				}
			}
		} catch (ex: Exception) {
			ex.printStackTrace()
		}
		return null
	}
}