package com.example.threadrxjava;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    @GET("users/{username}/repos")
    Call<List<Repo>> getRepos(@Path("username") String username);


    @GET("users/wanderingsword/repos")
    Call<List<Repo>> getRepos1();
}
