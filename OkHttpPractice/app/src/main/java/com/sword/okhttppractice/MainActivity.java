package com.sword.okhttppractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String url = "http://api.github.com/users/zerolans/repos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getFromUrl(String url) {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("failed~~~");
                }

                @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("success~~~~");

            }
        });
    }

    public void postToServer(String json, String url) {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed!!!!!!");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("success!!!!");
                System.out.println(response.body().toString());
            }
        });
    }

    public void cacheTest(String url) {
        File directory = new File(getCacheDir(), "http_cache");
        int maxSize = 50 * 1024 * 1024;

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(new Cache(directory, maxSize))
                .build();
    }
}
