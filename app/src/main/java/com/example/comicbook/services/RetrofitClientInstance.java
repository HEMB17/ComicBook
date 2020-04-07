package com.example.comicbook.services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://comicvine.gamespot.com/api/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .callTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);

            /**retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();**/
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            builder.client(httpClient.build());

            retrofit = builder.build();

        }
        return retrofit;
    }

}
