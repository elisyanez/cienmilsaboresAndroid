package com.ipgan.cienmilsaboresandroid.repository

import com.ipgan.cienmilsaboresandroid.model.Post
import com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance

class PostRepository {
    private val apiService = RetrofitInstance.api

    suspend fun getPosts(): List<Post>?{
        val response = apiService.getPosts()
        return if (response.isSuccessful) response.body()
        else {
        emptyList()
        }
    }

    suspend fun savePost(post: Post): Boolean {
        val response = apiService.createPost(post)
        return if (response!=null) true
        else false
    }


}