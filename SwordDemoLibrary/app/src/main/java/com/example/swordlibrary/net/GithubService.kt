package com.example.swordlibrary.net

import com.example.swordlibrary.githubApp.Repo
import com.example.swordlibrary.githubApp.User
import com.example.swordlibrary.githubApp.Widget
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*

/**
 * Retrofit Official Sample：https://square.github.io/retrofit/
 */
interface GithubService {
  @GET("user/{user}/repos")
  fun listRespos(@Path("user") user: String ): Call<List<Repo>>
  
  @GET("group/{id}/users")
  fun groupList(@Path("id") groupId: Int): Call<List<User>>
  
  @GET("group/{id}/users")
  fun groupList(@Path("id") groupId: Int, @Query("sort") sort: String): Call<List<User>>
  
  @GET("group/{id}/users")
  fun groupList(@Path("id") groupId: Int, @QueryMap options: Map<String, String>): Call<List<User>>
  
  @POST("users/new")
  fun createUser(@Body user: User): Call<User>
  
  //POST 请求，提交表单数据
  @FormUrlEncoded
  @POST("users/edit")
  fun updateUser(@Field("first_name") first: String, @Field("last_name") last: String): Call<User> 
  
  //post 请求，提交分块数据
  @Multipart
  @POST("user/photo")
  fun updateUser(@Part("photo") body: RequestBody, @Part("description") description: RequestBody): Call<User>
  
  //自定义 Header
  @Headers("Cache-Control: max-age=640000")
  @GET("widget/list")
  fun widgetList(): Call<List<Widget>>
  
  //自定义 Header
  @Headers(
    "Accept: application/vnd.github.v3.full+json",
    "User-Agent: Retrofit-Sample-App"
  )
  @GET("users/{username}")
  fun getUser(@Path("username") username: String): Call<User>
  
  //自定义 Header：@Header 注解
  @GET("users")
  fun getUserByAuthorization(@Header("Authorization") authorization: String): Call<User>
  
  //自定义 Header：@HeaderMap 注解
  @GET("users")
  fun getUser(@HeaderMap headers: Map<String, String>): Call<User>
}



fun main() {
  val githubService = Retrofit.Builder().baseUrl("https://api.github.com/").build().create(GithubService::class.java)
  githubService.listRespos("octocat")
}