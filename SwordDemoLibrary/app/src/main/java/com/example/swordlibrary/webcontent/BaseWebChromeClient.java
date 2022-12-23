package com.example.swordlibrary.webcontent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.swordlibrary.utils.SwordDialog;
import com.sword.LogUtil;

public class BaseWebChromeClient extends WebChromeClient {
    private final String tag = "BaseWebChromeClient";
    /*@Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return true;
    }*/

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        result.confirm();
        LogUtil.debug(tag, "onJsConfirm, url: " + url + ", message: " + message);
        new AlertDialog.Builder(view.getContext())
            .setMessage("onJsComfirm")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            })
            .show();
        return true;
        
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        LogUtil.debug(tag, "onJsPrompt, url: " + url + ", message: " + message);
        result.confirm("token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vMTkyLjE2OC4xOC42Njo5Mi9hcGkvY29kZUxvZ2luIiwiaWF0IjoxNjcwOTg2NDEwLCJleHAiOjE2NzE1MDQ4MTAsIm5iZiI6MTY3MDk4NjQxMCwianRpIjoiZkVvRWZqTFlVR2tUa1B1MiIsInN1YiI6MTEzNTYsInBydiI6ImM4ZWUxZmM4OWU3NzVlYzRjNzM4NjY3ZTViZTE3YTU5MGI2ZDQwZmMiLCJyb2xlIjoiYWNjb3VudCJ9.fW3ok61XikgOK9-qHz7XkjJbX077vVshxtJzlmim4HQ");
        return true;
        
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        LogUtil.debug(tag, "onJsPrompt, url: " + url + ", message: " + message);
        result.confirm();
        return true;
    }
}
