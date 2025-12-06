package com.ipgan.cienmilsaboresandroid.remote

import com.ipgan.cienmilsaboresandroid.model.Post
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>
    //suspend hace que la función se ejecute en un hilo separado,
    // es decir que tenga que esperar a que se complete la petición antes de continuar

    @GET("post/{id}")
    suspend fun getPostById(@Path("id") id: Int): Post

    @POST("posts")
    suspend fun createPost(@Body post: Post): Response<Post>

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)

}