package com.sword.webbasecontent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.utilclass.LogUtil;

import java.util.IllegalFormatCodePointException;

public class SwordWebChormClient extends WebChromeClient {
	private static final String tag = "SwordWebChormClient-";
	private SwordWebChormClient client;

	public void setClient(SwordWebChormClient client) {
		this.client = client;
	}

	@Override
	public void onCloseWindow(WebView window) {
		if (client != null) client.onCloseWindow(window);

		LogUtil.debug(tag + "onCloseWindow");
		super.onCloseWindow(window);
	}

	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

		LogUtil.debug(tag + "onConsoleMessage -- " + consoleMessage.message());
		if(client != null) return client.onConsoleMessage(consoleMessage);
		return super.onConsoleMessage(consoleMessage);
	}

	@Override
	public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
		LogUtil.debug(tag + "onCreateWindow - " + resultMsg);
		if (client != null) return client.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
		return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
	}

	@Override
	public void onGeolocationPermissionsHidePrompt() {
		LogUtil.debug(tag + "onGeolocationPermissionsHidePrompt");
		if (client != null) client.onGeolocationPermissionsHidePrompt();
		super.onGeolocationPermissionsHidePrompt();
	}

	@Override
	public void onHideCustomView() {
		LogUtil.debug(tag + "onHideCustomView");
		if (client != null) client.onHideCustomView();
		super.onHideCustomView();
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		LogUtil.debug(tag + "onJsAlert - JsResult: " + result);
		if (client != null) return client.onJsAlert(view, url, message, result);
		return super.onJsAlert(view, url, message, result);
	}

	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
		LogUtil.debug(tag + "onJsBeforeUnload");
		if (client != null) return client.onJsBeforeUnload(view, url, message, result);
		return super.onJsBeforeUnload(view, url, message, result);
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
		LogUtil.debug(tag + "onJsConfirm - JsResult: " + result);
		if (client != null) return client.onJsConfirm(view, url, message, result);
		return super.onJsConfirm(view, url, message, result);
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
		LogUtil.debug(tag + "- onJsPrompt - JsPromptResult : " + result);
		if (client != null) return client.onJsPrompt(view, url, message, defaultValue, result);
		return super.onJsPrompt(view, url, message, defaultValue, result);
	}

	@Override
	public void onPermissionRequest(PermissionRequest request) {
		LogUtil.debug(tag + "onPermissionRequest");
		if (client != null) client.onPermissionRequest(request);
		super.onPermissionRequest(request);
	}

	@Override
	public void onPermissionRequestCanceled(PermissionRequest request) {
		LogUtil.debug(tag + "onPermissionRequestCanceled");
		if (client != null) client.onPermissionRequestCanceled(request);
		super.onPermissionRequestCanceled(request);
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		LogUtil.debug("onProgressChanged, progress: " + newProgress);
		if (client != null) client.onProgressChanged(view, newProgress);
		super.onProgressChanged(view, newProgress);
	}

	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		LogUtil.debug(tag + "onReceivedIcon");
		if (client != null) client.onReceivedIcon(view, icon);
		super.onReceivedIcon(view, icon);
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		LogUtil.debug(tag + "onReceivedTitle - title: " + title);
		if (client != null) client.onReceivedTitle(view, title);
		super.onReceivedTitle(view, title);
	}

	@Override
	public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
		LogUtil.debug(tag + "onReceivedTouchIconUrl - url: " + url);
		if (client != null) client.onReceivedTouchIconUrl(view, url, precomposed);
		super.onReceivedTouchIconUrl(view, url, precomposed);
	}

	@Override
	public void onRequestFocus(WebView view) {
		LogUtil.debug(tag + "onRequestFocus");
		if (client != null) client.onRequestFocus(view);
		super.onRequestFocus(view);
	}

	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		LogUtil.debug(tag + "onShowCustomView");
		if (client != null) client.onShowCustomView(view, callback);
		super.onShowCustomView(view, callback);
	}

	@Override
	public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
		LogUtil.debug(tag + "onShowFileChooser");
		if (client != null) client.onShowFileChooser(webView, filePathCallback, fileChooserParams);
		return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
	}
}
