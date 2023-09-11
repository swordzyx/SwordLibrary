package sword.webcontent;

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
import sword.SwordLog;

public class BaseWebClient extends WebViewClient {
  private final ProgressBar progressBar;
  private final BaseWebClient client;

  public BaseWebClient(ProgressBar progressBar) {
    this(progressBar, null);
  }
  
  public BaseWebClient(ProgressBar progressBar, BaseWebClient client) {
    this.progressBar = progressBar;
    this.client = client; 
  }


  @Override
  public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
    SwordLog.debug("doUpdateVisitedHistory");
    if (client != null) client.doUpdateVisitedHistory(view, url, isReload);
    super.doUpdateVisitedHistory(view, url, isReload);
  }

  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    super.onPageStarted(view, url, favicon);
    progressBar.setVisibility(View.VISIBLE);
    SwordLog.debug("start page");
    
    

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
    SwordLog.debug("start finish");
    
    if (client != null) client.onPageFinished(view, url);
  }

  @Override
  public void onFormResubmission(WebView view, Message dontResend, Message resend) {
    SwordLog.debug("onFormResubmission");
    super.onFormResubmission(view, dontResend, resend);
    
    if (client != null) client.onFormResubmission(view, dontResend, resend);
  }

  @Override
  public void onLoadResource(WebView view, String url) {
    SwordLog.debug("onLoadResource");
    super.onLoadResource(view, url);
    
    if (client != null) client.onLoadResource(view, url);
  }

  @Override
  public void onPageCommitVisible(WebView view, String url) {
    SwordLog.debug("onPageCommit Visible");
    super.onPageCommitVisible(view, url);
    
    
    
    if (client != null) client.onPageCommitVisible(view, url);
    
    
  }

  @Override
  public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
    SwordLog.debug("onReceivedClientCertRequest");
    super.onReceivedClientCertRequest(view, request);
 
    if (client != null) client.onReceivedClientCertRequest(view, request);
   }

  @Override
  public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
    SwordLog.debug("onReceivedError");
    super.onReceivedError(view, request, error);
    
    if (client != null) client.onReceivedError(view, request, error);
  }

  @Override
  public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
    SwordLog.debug("onReceivedHttpAuthRequest");
    super.onReceivedHttpAuthRequest(view, handler, host, realm);
    if (client != null) client.onReceivedHttpAuthRequest(view, handler, host, realm);
  }

  @Override
  public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
    SwordLog.debug("onReceivedHttpError");
    super.onReceivedHttpError(view, request, errorResponse);
    if (client != null) client.onReceivedHttpError(view, request, errorResponse);
  }

  @Override
  public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
    SwordLog.debug("onReceivedLoginRequest");
    super.onReceivedLoginRequest(view, realm, account, args);

    if (client != null) client.onReceivedLoginRequest(view, realm, account, args);
  }

  @Override
  public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
    SwordLog.debug("onReceivedSslError");
    super.onReceivedSslError(view, handler, error);

    if (client != null) client.onReceivedSslError(view, handler, error);
  }

  @Override
  public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
    SwordLog.debug("onRenderProcessGone");
    if (client != null) return client.onRenderProcessGone(view, detail);

    return super.onRenderProcessGone(view, detail);
  }

  @Override
  public void onScaleChanged(WebView view, float oldScale, float newScale) {
    SwordLog.debug("onScaleChange");
    super.onScaleChanged(view, oldScale, newScale);

    if (client != null) client.onScaleChanged(view, oldScale, newScale);
  }

  @Override
  public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
    SwordLog.debug("onUnhandledKeyEvent event: " + event.getAction() + "-" + event.getCharacters());
    super.onUnhandledKeyEvent(view, event);

    if (client != null) client.onUnhandledKeyEvent(view, event);
  }

  @Nullable
  @Override
  public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
    SwordLog.debug("shouldInterceptRequest, url: " + request.getUrl().toString());

    if (client != null) return client.shouldInterceptRequest(view, request);

    return super.shouldInterceptRequest(view, request);
  }

  @Override
  public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
    SwordLog.debug("shouldOverrideKeyEvent");
    if (client != null) return client.shouldOverrideKeyEvent(view, event);

    return super.shouldOverrideKeyEvent(view, event);
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    SwordLog.debug("shouldOverrideUrlLoading, uri: " + url);
    return true;
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
    SwordLog.debug("shouldOverrideUrlLoading, uri: " + request.getUrl().toString());
    
    if (client != null) return client.shouldOverrideUrlLoading(view, request);

    return true;
  }
}
