package com.example.threadrxjava;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    @GET("users/{username}/repos")
    Single<List<Repo>> getRepos(@Path("username") String username);

    @GET("users/{username}/repos")
    Call<List<Repo>> getCallRepos(@Path("username") String username);
}
