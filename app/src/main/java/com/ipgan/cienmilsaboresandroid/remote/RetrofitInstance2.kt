package com.ipgan.cienmilsaboresandroid.remote

import android.content.Context
import com.google.gson.GsonBuilder
import com.ipgan.cienmilsaboresandroid.config.NgrokManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.ipgan.cienmilsaboresandroid.config.TokenManager // <-- 1. IMPORTAMOS TOKEN MANAGER
import okhttp3.logging.HttpLoggingInterceptor
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitInstance2 {

    @Volatile
    private var apiService: ApiService2? = null
    private var currentUrl: String = ""

    fun getApi(context: Context): ApiService2 {
        synchronized(this) {
            val baseUrl = NgrokManager.getBaseUrl(context)
            if (apiService != null && baseUrl == currentUrl) {
                return apiService!!
            }
            currentUrl = baseUrl

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            //Interceptor de Token
            val authInterceptor = okhttp3.Interceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                // Obtenemos el token guardado
                val token = TokenManager.getToken(context)
                if (token != null) {
                    // Si hay token, lo añadimos a la cabecera 'Authorization'
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }

            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            // 3. AÑADIMOS EL INTERCEPTOR AL CLIENTE HTTP

            val client = OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(authInterceptor) // <-- LO AÑADIMOS AQUÍ
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(currentUrl)
                .client(client) // Usamos el nuevo cliente con el interceptor
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()

            apiService = retrofit.create(ApiService2::class.java)
            return apiService!!
        }
    }

    fun invalidate() {
        synchronized(this) {
            apiService = null
        }
    }
}