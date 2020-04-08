package com.example.comicbook.services;

import com.example.comicbook.models.BookItem;
import com.example.comicbook.models.DetailBookItem;
import com.example.comicbook.models.ImageModel;
import com.example.comicbook.models.ResultModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface GetDataService {
    @GET("issues/?format=json&field_list=api_detail_url,date_added,name,issue_number,image")
    Call<ResultModel<List<BookItem>>> getAllBooks(@Query("api_key") String api_key, @Query("offset") int offset, @Query("limit") int limit);

    @GET
    Call<ResultModel<DetailBookItem>> getSubItem(@Url String url);

    @GET
    Call<ResultModel<DetailBookItem>> getDetailBookImage(@Url String url);
}
