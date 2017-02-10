package com.example.root.networkexample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nantha.kumar on 18/12/16.
 */

public interface GitAPI {
    String END_POINT = "https://api.github.com";

    @GET("/users/{user}")
    Call<GitHubUser> getUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Call<List<GitHubRepo>> getRepos(@Path("user") String user);

}
