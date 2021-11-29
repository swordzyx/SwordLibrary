package com.sword.floatball;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.utilclass.LogUtil;

public class SwordWebChormClient extends WebChromeClient {
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		LogUtil.debug("onProgressChanged, progress: " + newProgress);
		super.onProgressChanged(view, newProgress);
	}
}
