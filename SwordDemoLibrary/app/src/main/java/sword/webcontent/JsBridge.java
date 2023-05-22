package sword.webcontent;

import android.net.Uri;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.sword.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class JsBridge {
  private final String tag = "JsBridge";
  
  private final String METHOD_SCHEME = "method";

  /**
   * SDK 登出 
   */
  @JavascriptInterface
  public void logout(String msg) {
    LogUtil.debug(tag, msg);
  }

  /**
   * 返回 String 给 WebView 
   */
  @JavascriptInterface
  public String getInfo() {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("phone", "12345667");
      jsonObject.put("userName", "zaaaaa");
      jsonObject.put("age", 34);
      jsonObject.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vMTkyLjE2OC4xOC42Njo5Mi9hcGkvY29kZUxvZ2luIiwiaWF0IjoxNjcwOTg2NDEwLCJleHAiOjE2NzE1MDQ4MTAsIm5iZiI6MTY3MDk4NjQxMCwianRpIjoiZkVvRWZqTFlVR2tUa1B1MiIsInN1YiI6MTEzNTYsInBydiI6ImM4ZWUxZmM4OWU3NzVlYzRjNzM4NjY3ZTViZTE3YTU5MGI2ZDQwZmMiLCJyb2xlIjoiYWNjb3VudCJ9.fW3ok61XikgOK9-qHz7XkjJbX077vVshxtJzlmim4HQ");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return jsonObject.toString();
  }

  /**
   * 解析 method://。。。。 协议，并执行对应的方法
   */
  public static void parseMethodUri(Uri uri) {
    
  }

  /**
   * 执行 Web 端的 js 方法，并获取返回结果
   * 
   * 可用于传递内容给 WebView
   */
  public void returnToWebView(WebView webView, String info) {
    //webView.loadUrl("javascript:receiveFromNative(\"" + info + "\")"); Android 4.2 以下使用
    webView.evaluateJavascript("javascript:receiveFromNative(\"" + info + "\")", new ValueCallback<String>() {
      @Override
      public void onReceiveValue(String value) {
        LogUtil.debug(tag, "evaluatejavascript receive: " + value);
      }
    });
  }
}
