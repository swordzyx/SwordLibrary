package com.sword.opensourceframework.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("user/{user}/repos")
    Call<List<Repo>> lisRepos(@Path("user") String user);
}
