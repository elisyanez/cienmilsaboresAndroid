package com.ipgan.cienmilsaboresandroid.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance{
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            //permite conectarse: abrir y cerrar conexiones
            .build()
            .create(ApiService::class.java)
    }
}