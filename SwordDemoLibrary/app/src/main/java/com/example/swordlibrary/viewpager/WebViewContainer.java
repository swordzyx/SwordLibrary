package com.example.swordlibrary.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.swordlibrary.webcontent.BaseWebChromeClient;
import com.example.swordlibrary.webcontent.BaseWebClient;
import com.example.swordlibrary.webcontent.JsBridge;

public class WebViewContainer extends ViewGroup {
  private boolean isAttachedToWindow = false;
  
  private final WebView webView;
  private final ProgressBar progressBar;
  private final Context context;
  
  public WebViewContainer(@NonNull Context context) {
    this(context, null);
  }

  @SuppressLint({"SetJavaScriptEnabled"})
  public WebViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    this.context = context;

    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    webView = new WebView(context);
    WebSettings webSettings = webView.getSettings();
    
    webSettings.setJavaScriptEnabled(true);
    webView.addJavascriptInterface(new JsBridge(), "jsBridge");
    addView(webView, params);

    progressBar = new ProgressBar(context);
    addView(progressBar, params);

    webView.setWebViewClient(new BaseWebClient(progressBar));
    webView.setWebChromeClient(new BaseWebChromeClient());
    
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
    int height = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.AT_MOST);
    
    progressBar.measure(width, height);
    webView.measure(width, MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
    
    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    progressBar.layout(0, 0, progressBar.getMeasuredWidth(), progressBar.getMeasuredHeight());
    webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    isAttachedToWindow = true;
  }

  public void loadUrl(String url) {
    if (webView == null || !isAttachedToWindow) {
      return;
    }

    webView.loadUrl(url);
  }
  

}
