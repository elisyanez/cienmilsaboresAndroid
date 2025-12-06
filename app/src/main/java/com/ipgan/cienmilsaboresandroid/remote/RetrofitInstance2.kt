package com.ipgan.cienmilsaboresandroid.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance2{
    val api: ApiService2 by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            //permite conectarse: abrir y cerrar conexiones
            .build()
            .create(ApiService2::class.java)
    }
}