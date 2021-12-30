package com.sword.httptheory.okhttp;

import androidx.annotation.NonNull;

import com.example.utilclass.LogUtil;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpSampleJava {
  public static void main(String[] args) {
    
  }
  
  void okHttpSample() {
    OkHttpClient client = new OkHttpClient.Builder().build();
    
    Request request = new Request.Builder()
        .url("http://hencoder.com")
        .build();
    
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(@NonNull Call call, @NonNull IOException e) {
        
      }

      @Override
      public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        LogUtil.debug("okhttp response", Objects.requireNonNull(response.body()).string());
      }
    });
  }
}
