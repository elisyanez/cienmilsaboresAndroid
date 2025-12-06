package com.ipgan.cienmilsaboresandroid.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Esta clase se utiliza para crear una instancia de Retrofit
//Conectando con API externa JSONPlaceholder
object RetrofitInstance{
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}