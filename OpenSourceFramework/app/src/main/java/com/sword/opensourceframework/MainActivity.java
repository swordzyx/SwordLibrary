package com.sword.opensourceframework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sword.opensourceframework.retrofit.Repo;
import com.sword.opensourceframework.retrofit.RetrofitService;

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

        Call<List<Repo>> repos = retrofitService.lisRepos("zerolans");

        repos.enqueue(callback);
    }

    Callback callback = new Callback<List<Repo>>() {
        @Override
        public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
            System.out.println("onResponse");
            List<Repo> result = response.body();
            System.out.println("response = " + result.get(0));
        }

        @Override
        public void onFailure(Call<List<Repo>> call, Throwable t) {

        }
    };
}
