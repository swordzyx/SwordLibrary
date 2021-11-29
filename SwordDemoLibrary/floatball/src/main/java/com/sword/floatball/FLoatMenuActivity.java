package com.sword.floatball;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FLoatMenuActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    WebView webView = loadPageFromUrl("http://192.168.18.86:8080/feedback");
    setContentView(webView);
  }
  
  @SuppressLint("SetJavaScriptEnabled")
  private WebView loadPageFromUrl(String url) {
    WebView webView = new WebView(this);
    webView.loadUrl(url);
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    return webView;
  }
}
