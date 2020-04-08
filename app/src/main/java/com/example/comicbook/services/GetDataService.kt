package com.example.comicbook.services

import com.example.comicbook.models.BookItem
import com.example.comicbook.models.DetailBookItem
import com.example.comicbook.models.ResultModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GetDataService {
    @get:GET("issues/?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=api_detail_url,date_added,name,issue_number,image")
    val allBooks: Call<ResultModel<List<BookItem>>>

    @GET
    fun getSubItem(@Url url: String?): Call<ResultModel<DetailBookItem>>

    @GET
    fun getDetailBookImage(@Url url: String?): Call<ResultModel<DetailBookItem>>
}