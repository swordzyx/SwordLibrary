package com.sword.floatball;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FLoatMenuActivity extends AppCompatActivity {
  WebView webView;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_menu_activity);
    webView = findViewById(R.id.webView);

    loadPageFromUrl("http://192.168.18.86:8080/feedback");
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void loadPageFromUrl(String url) {
    if (webView == null) {
      webView = new WebView(this);

    }

    FloatMenuWebClient webClient = new FloatMenuWebClient(findViewById(R.id.progress));
    webView.setWebViewClient(webClient);

    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);

    webView.loadUrl(url);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK && webView != null && webView.canGoBack()) {
      webView.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }
}
