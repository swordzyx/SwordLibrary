package com.sword.floatball;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.example.utilclass.LogUtil;

public class FloatMenuWebClient extends WebViewClient {
  private final ProgressBar progressBar;
  private final FloatMenuWebClient client;

  public FloatMenuWebClient(ProgressBar progressBar) {
    this(progressBar, null);
  }
  
  public FloatMenuWebClient(ProgressBar progressBar, FloatMenuWebClient client) {
    this.progressBar = progressBar;
    this.client = client; 
  }


  @Override
  public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
    LogUtil.debug("doUpdateVisitedHistory");
    if (client != null) client.doUpdateVisitedHistory(view, url, isReload);
    super.doUpdateVisitedHistory(view, url, isReload);
  }

  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    super.onPageStarted(view, url, favicon);
    progressBar.setVisibility(View.VISIBLE);
    LogUtil.debug("start page");

    if (client != null) client.onPageStarted(view, url, favicon);
  }

  @Override
  public void onPageFinished(WebView view, String url) {
    super.onPageFinished(view, url);
    if (view.getProgress() < 100) {
      progressBar.setProgress(view.getProgress());
    } else {
      progressBar.setVisibility(View.INVISIBLE);
    }
    LogUtil.debug("start finish");
    
    if (client != null) client.onPageFinished(view, url);
  }

  @Override
  public void onFormResubmission(WebView view, Message dontResend, Message resend) {
    LogUtil.debug("onFormResubmission");
    super.onFormResubmission(view, dontResend, resend);
    
    if (client != null) client.onFormResubmission(view, dontResend, resend);
  }

  @Override
  public void onLoadResource(WebView view, String url) {
    LogUtil.debug("onLoadResource");
    super.onLoadResource(view, url);
    
    if (client != null) client.onLoadResource(view, url);
  }

  @Override
  public void onPageCommitVisible(WebView view, String url) {
    LogUtil.debug("onPageCommit Visible");
    super.onPageCommitVisible(view, url);
    
    if (client != null) client.onPageCommitVisible(view, url);
  }

  @Override
  public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
    LogUtil.debug("onReceivedClientCertRequest");
    super.onReceivedClientCertRequest(view, request);
 
    if (client != null) client.onReceivedClientCertRequest(view, request);
   }

  @Override
  public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
    LogUtil.debug("onReceivedError");
    super.onReceivedError(view, request, error);
    
    if (client != null) client.onReceivedError(view, request, error);
  }

  @Override
  public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
    LogUtil.debug("onReceivedHttpAuthRequest");
    super.onReceivedHttpAuthRequest(view, handler, host, realm);
  }

  @Override
  public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
    LogUtil.debug("onReceivedHttpError");
    super.onReceivedHttpError(view, request, errorResponse);
  }

  @Override
  public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
    LogUtil.debug("onReceivedLoginRequest");
    super.onReceivedLoginRequest(view, realm, account, args);
  }

  @Override
  public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
    LogUtil.debug("onReceivedSslError");
    super.onReceivedSslError(view, handler, error);
  }

  @Override
  public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
    LogUtil.debug("onRenderProcessGone");
    return super.onRenderProcessGone(view, detail);
  }

  @Override
  public void onScaleChanged(WebView view, float oldScale, float newScale) {
    LogUtil.debug("onScaleChange");
    super.onScaleChanged(view, oldScale, newScale);
  }

  @Override
  public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
    LogUtil.debug("onUnhandledKeyEvent event: " + event.getAction() + "-" + event.getCharacters());
    super.onUnhandledKeyEvent(view, event);
  }

  @Nullable
  @Override
  public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
    LogUtil.debug("shouldInterceptRequest");
    return super.shouldInterceptRequest(view, request);
  }

  @Override
  public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
    LogUtil.debug("shouldOverrideKeyEvent");
    return super.shouldOverrideKeyEvent(view, event);
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
    LogUtil.debug("shouldOverrideUrlLoading");
    return super.shouldOverrideUrlLoading(view, request);
  }
}
