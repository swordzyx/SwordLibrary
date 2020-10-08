package com.sword.retrofitlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        Call<List<Repo>> call = retrofitService.getRepos("zerolans");

        call.enqueue(mCallback);
    }

    Callback mCallback = new Callback() {
        @Override
        public void onResponse(Call call, Response response) {
            System.out.println("onResponse");
            System.out.println("result: " + response.toString());
        }

        @Override
        public void onFailure(Call call, Throwable t) {

        }
    };
}