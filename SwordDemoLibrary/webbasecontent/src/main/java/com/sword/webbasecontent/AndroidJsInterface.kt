package com.sword.webbasecontent

import android.webkit.JavascriptInterface

class AndroidJsInterface {
	@JavascriptInterface
	fun hello(msg: String) {
		println("Js 执行 AndroidJsInterface#hello($msg)")
	}

}