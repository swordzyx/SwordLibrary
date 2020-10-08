package com.sword.retrofitlearning;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("/users/{user}/repos")
    Call<List<Repo>> getRepos(@Path("user") String user);
}
