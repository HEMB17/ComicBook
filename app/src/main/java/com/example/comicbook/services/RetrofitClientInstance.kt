package com.example.comicbook.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://comicvine.gamespot.com/api/"
    /**retrofit = new retrofit2.Retrofit.Builder()
     * .baseUrl(BASE_URL)
     * .addConverterFactory(GsonConverterFactory.create())
     * .build(); */
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                val httpClient = OkHttpClient.Builder()
                        .callTimeout(2, TimeUnit.MINUTES)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                /**retrofit = new retrofit2.Retrofit.Builder()
                 * .baseUrl(BASE_URL)
                 * .addConverterFactory(GsonConverterFactory.create())
                 * .build(); */
                val builder = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                builder.client(httpClient.build())
                retrofit = builder.build()
            }
            return retrofit
        }
}