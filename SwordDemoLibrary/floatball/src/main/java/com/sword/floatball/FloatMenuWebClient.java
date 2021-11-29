package com.sword.floatball;

import android.graphics.Bitmap;
import android.os.Message;
import android.webkit.ClientCertRequest;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.utilclass.LogUtil;

public class FloatMenuWebClient extends WebViewClient {
  @Override
  public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
    LogUtil.debug("doUpdateVisitedHistory");
    super.doUpdateVisitedHistory(view, url, isReload);
  }

  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    super.onPageStarted(view, url, favicon);
    LogUtil.debug("start page");
  }

  @Override
  public void onPageFinished(WebView view, String url) {
    super.onPageFinished(view, url);
    LogUtil.debug("start finish");
  }

  @Override
  public void onFormResubmission(WebView view, Message dontResend, Message resend) {
    LogUtil.debug("onFormResubmission");
    super.onFormResubmission(view, dontResend, resend);
  }

  @Override
  public void onLoadResource(WebView view, String url) {
    LogUtil.debug("onLoadResource");
    super.onLoadResource(view, url);
  }

  @Override
  public void onPageCommitVisible(WebView view, String url) {
    LogUtil.debug("onPageCommit Visible");
    super.onPageCommitVisible(view, url);
  }

  @Override
  public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
    LogUtil.debug("onReceivedClientCertRequest");
    super.onReceivedClientCertRequest(view, request);
  }

  @Override
  public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
    
    super.onReceivedError(view, request, error);
  }
}
