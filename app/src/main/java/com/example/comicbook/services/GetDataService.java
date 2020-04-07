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
import retrofit2.http.Url;

public interface GetDataService {
    @GET("issues/?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=api_detail_url,date_added,name,issue_number,image")
    Call<ResultModel<List<BookItem>>> getAllBooks();

    @GET
    Call<ResultModel<DetailBookItem>> getSubItem(@Url String url);

    @GET
    Call<ResultModel<DetailBookItem>> getDetailBookImage(@Url String url);
}
