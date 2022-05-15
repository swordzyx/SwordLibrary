package com.httptheory.retrofit;

import com.sword.httptheory.retrofit.Repo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);

    @GET("users/{user}/repos")
    Single<List<Repo>> listReposRxjava(@Path("user") String user);
}
