package com.example.comicbook.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comicbook.R
import com.example.comicbook.adapters.RecyclerDetailBookAdapter
import com.example.comicbook.models.DetailBookItem
import com.example.comicbook.models.ResultModel
import com.example.comicbook.services.SubItemModel
import com.example.comicbook.services.GetDataService
import com.example.comicbook.services.RetrofitClientInstance
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*

class DetailComicActivity : AppCompatActivity() {
    private var recyclerCharacters: RecyclerView? = null
    private var recyclerTeams: RecyclerView? = null
    private var recyclerLocations: RecyclerView? = null
    private var recyclerConcepts: RecyclerView? = null
    private var mAdapterCharacters: RecyclerView.Adapter<*>? = null
    private var mAdapterTeams: RecyclerView.Adapter<*>? = null
    private var mAdapterLocations: RecyclerView.Adapter<*>? = null
    private var mAdapterConcepts: RecyclerView.Adapter<*>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    var mList: List<DetailBookItem> = ArrayList()
    var progressDoalog: ProgressDialog? = null
    var listDetailBookCharacters: MutableList<DetailBookItem> = ArrayList()
    var listDetailBookTeams: MutableList<DetailBookItem> = ArrayList()
    var listDetailBookLocations: MutableList<DetailBookItem> = ArrayList()
    var listDetailBookConcepts: MutableList<DetailBookItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_comic)
        recyclerCharacters = findViewById(R.id.recyclerCharacters)
        recyclerTeams = findViewById(R.id.recyclerTeams)
        recyclerLocations = findViewById(R.id.recyclerLocations)
        recyclerConcepts = findViewById(R.id.recyclerConcepts)
        val imageComic = findViewById<ImageView>(R.id.imageComic)
        val original_url_book = intent.getStringExtra("original_url_book")
        val api_detail_url = intent.getStringExtra("api_detail_url")
        Picasso.get()
                .load(original_url_book)
                .resize(500, 750)
                .centerCrop()
                .into(imageComic)
        queryBookDetail("Characters", api_detail_url)
        queryBookDetail("Teams", api_detail_url)
        queryBookDetail("Locations", api_detail_url)
        queryBookDetail("Concepts", api_detail_url)
    }

    private fun queryBookDetail(typeQuery: String, api_detail_url: String) {
        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
        var call = service.getSubItem("$api_detail_url?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=character_credits")
        if (typeQuery == "Characters") {
            call = service.getSubItem("$api_detail_url?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=character_credits")
        } else if (typeQuery == "Teams") {
            call = service.getSubItem("$api_detail_url?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=team_credits")
        } else if (typeQuery == "Locations") {
            call = service.getSubItem("$api_detail_url?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=location_credits")
        } else if (typeQuery == "Concepts") {
            call = service.getSubItem("$api_detail_url?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=concept_credits")
        }

        call.enqueue(object : Callback<ResultModel<DetailBookItem>> {
            override fun onResponse(call: Call<ResultModel<DetailBookItem>>, response: Response<ResultModel<DetailBookItem>>) {
                if (typeQuery == "Characters") {
                    queryNameAndImage(response.body()!!.results.character_credits, "Characters")
                    generateDataListCharacters()
                } else if (typeQuery == "Teams") {
                    queryNameAndImage(response.body()!!.results.team_credits, "Teams")
                    generateDataListTeams()
                } else if (typeQuery == "Locations") {
                    queryNameAndImage(response.body()!!.results.location_credits, "Locations")
                    generateDataListLocations()
                } else if (typeQuery == "Concepts") {
                    queryNameAndImage(response.body()!!.results.concept_credits, "Concepts")
                    generateDataListConcepts()
                }
            }

            override fun onFailure(call: Call<ResultModel<DetailBookItem>>, error: Throwable) {
                if (error is SocketTimeoutException) {
                    Toast.makeText(this@DetailComicActivity, "Connection Timeout", Toast.LENGTH_SHORT).show()
                } else if (error is IOException) {
                    Toast.makeText(this@DetailComicActivity, "Timeout", Toast.LENGTH_SHORT).show()
                } else { //Call was cancelled by user
                    if (call.isCanceled) {
                        Toast.makeText(this@DetailComicActivity, "Call was cancelled forcefully", Toast.LENGTH_SHORT).show()
                    } else { //Generic error handling
                        Toast.makeText(this@DetailComicActivity, "Network Error :: " + error.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun queryNameAndImage(subItemModels: List<SubItemModel>?, typeQuery: String) {
        if (subItemModels != null) {
            for (subItemModel in subItemModels) {
                val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
                val call = service.getDetailBookImage(subItemModel.api_detail_url + "?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=image,name")
                call?.enqueue(object : Callback<ResultModel<DetailBookItem>> {
                    override fun onResponse(call: Call<ResultModel<DetailBookItem>>, response: Response<ResultModel<DetailBookItem>>) {
                        if (typeQuery == "Characters") {
                            listDetailBookCharacters.add(response.body()!!.results)
                            mAdapterCharacters!!.notifyDataSetChanged()
                        } else if (typeQuery == "Teams") {
                            listDetailBookTeams.add(response.body()!!.results)
                            mAdapterTeams!!.notifyDataSetChanged()
                        } else if (typeQuery == "Locations") {
                            listDetailBookLocations.add(response.body()!!.results)
                            mAdapterLocations!!.notifyDataSetChanged()
                        } else if (typeQuery == "Concepts") {
                            listDetailBookConcepts.add(response.body()!!.results)
                            mAdapterConcepts!!.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<ResultModel<DetailBookItem>>, error: Throwable) {
                        progressDoalog!!.dismiss()
                        if (error is SocketTimeoutException) {
                            Toast.makeText(this@DetailComicActivity, "Connection Timeout", Toast.LENGTH_SHORT).show()
                        } else if (error is IOException) {
                            Toast.makeText(this@DetailComicActivity, "Timeout", Toast.LENGTH_SHORT).show()
                        } else { //Call was cancelled by user
                            if (call.isCanceled) {
                                Toast.makeText(this@DetailComicActivity, "Call was cancelled forcefully", Toast.LENGTH_SHORT).show()
                            } else { //Generic error handling
                                Toast.makeText(this@DetailComicActivity, "Network Error :: " + error.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun generateDataListCharacters() {
        gridLayoutManager = GridLayoutManager(this, 2)
        mAdapterCharacters = RecyclerDetailBookAdapter(listDetailBookCharacters, this, gridLayoutManager!!)
        recyclerCharacters!!.adapter = mAdapterCharacters
        recyclerCharacters!!.layoutManager = gridLayoutManager
    }

    private fun generateDataListTeams() {
        gridLayoutManager = GridLayoutManager(this, 2)
        mAdapterTeams = RecyclerDetailBookAdapter(listDetailBookTeams, this, gridLayoutManager!!)
        recyclerTeams!!.adapter = mAdapterTeams
        recyclerTeams!!.layoutManager = gridLayoutManager
    }

    private fun generateDataListLocations() {
        gridLayoutManager = GridLayoutManager(this, 2)
        mAdapterLocations = RecyclerDetailBookAdapter(listDetailBookLocations, this, gridLayoutManager!!)
        recyclerLocations!!.adapter = mAdapterLocations
        recyclerLocations!!.layoutManager = gridLayoutManager
    }

    private fun generateDataListConcepts() {
        gridLayoutManager = GridLayoutManager(this, 2)
        mAdapterConcepts = RecyclerDetailBookAdapter(listDetailBookConcepts, this, gridLayoutManager!!)
        recyclerConcepts!!.adapter = mAdapterConcepts
        recyclerConcepts!!.layoutManager = gridLayoutManager
    }
}