package com.example.comicbook.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.comicbook.R;
import com.example.comicbook.adapters.RecyclerBookAdapter;
import com.example.comicbook.adapters.RecyclerDetailBookAdapter;
import com.example.comicbook.models.DetailBookItem;
import com.example.comicbook.models.ImageModel;
import com.example.comicbook.models.ResultModel;
import com.example.comicbook.models.SubItemModel;
import com.example.comicbook.services.GetDataService;
import com.example.comicbook.services.RetrofitClientInstance;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailComicActivity extends AppCompatActivity {

    private RecyclerView recyclerCharacters;
    private RecyclerView recyclerTeams;
    private RecyclerView recyclerLocations;
    private RecyclerView recyclerConcepts;

    private RecyclerView.Adapter mAdapterCharacters;
    private RecyclerView.Adapter mAdapterTeams;
    private RecyclerView.Adapter mAdapterLocations;
    private RecyclerView.Adapter mAdapterConcepts;

    private GridLayoutManager gridLayoutManager;
    List<DetailBookItem> mList = new ArrayList<>();
    ProgressDialog progressDoalog;

    List<DetailBookItem> listDetailBookCharacters = new ArrayList<>();
    List<DetailBookItem> listDetailBookTeams = new ArrayList<>();
    List<DetailBookItem> listDetailBookLocations = new ArrayList<>();
    List<DetailBookItem> listDetailBookConcepts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_comic);

        recyclerCharacters = findViewById(R.id.recyclerCharacters);
        recyclerTeams = findViewById(R.id.recyclerTeams);
        recyclerLocations = findViewById(R.id.recyclerLocations);
        recyclerConcepts = findViewById(R.id.recyclerConcepts);
        ImageView imageComic = findViewById(R.id.imageComic);

        String original_url_book = getIntent().getStringExtra("original_url_book");
        String api_detail_url = getIntent().getStringExtra("api_detail_url");

        Picasso.get()
                .load(original_url_book)
                .resize(500, 750)
                .centerCrop()
                .into(imageComic);

        queryBookDetail("Characters", api_detail_url);
        queryBookDetail("Teams", api_detail_url);
        queryBookDetail("Locations", api_detail_url);
        queryBookDetail("Concepts", api_detail_url);

    }

    private void queryBookDetail(String typeQuery, String api_detail_url){

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResultModel<DetailBookItem>> call = null;
        if(typeQuery.equals("Characters")) {
            call = service.getSubItem(api_detail_url + "?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=character_credits");
        }else if(typeQuery.equals("Teams")) {
            call = service.getSubItem(api_detail_url + "?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=team_credits");
        }else if(typeQuery.equals("Locations")) {
            call = service.getSubItem(api_detail_url + "?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=location_credits");
        }else if(typeQuery.equals("Concepts")) {
            call = service.getSubItem(api_detail_url + "?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=concept_credits");
        };
        call.enqueue(new Callback<ResultModel<DetailBookItem>>() {
            @Override
            public void onResponse(Call<ResultModel<DetailBookItem>> call, Response<ResultModel<DetailBookItem>> response) {
                if(typeQuery.equals("Characters")) {
                    queryNameAndImage(response.body().getResults().getCharacter_credits(), "Characters");
                    generateDataListCharacters();
                }else if(typeQuery.equals("Teams")){
                    queryNameAndImage(response.body().getResults().getTeam_credits(), "Teams");
                    generateDataListTeams();
                }else if(typeQuery.equals("Locations")){
                    queryNameAndImage(response.body().getResults().getLocation_credits(), "Locations");
                    generateDataListLocations();
                }else if(typeQuery.equals("Concepts")){
                    queryNameAndImage(response.body().getResults().getConcept_credits(), "Concepts");
                    generateDataListConcepts();
                }
            }

            @Override
            public void onFailure(Call<ResultModel<DetailBookItem>> call, Throwable error) {
                if (error instanceof SocketTimeoutException)
                {
                    Toast.makeText(DetailComicActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof IOException)
                {
                    Toast.makeText(DetailComicActivity.this, "Timeout", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Call was cancelled by user
                    if(call.isCanceled())
                    {
                        Toast.makeText(DetailComicActivity.this, "Call was cancelled forcefully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Generic error handling
                        Toast.makeText(DetailComicActivity.this, "Network Error :: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void queryNameAndImage(List<SubItemModel> subItemModels, String typeQuery) {

        if(subItemModels != null) {
            for (SubItemModel subItemModel : subItemModels) {

                GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<ResultModel<DetailBookItem>> call = service.getDetailBookImage(subItemModel.getApi_detail_url() + "?api_key=1ed7967e84b13642e18f74eae0c8e399d0ddbe9b&format=json&field_list=image,name");
                call.enqueue(new Callback<ResultModel<DetailBookItem>>() {
                    @Override
                    public void onResponse(Call<ResultModel<DetailBookItem>> call, Response<ResultModel<DetailBookItem>> response) {
                        if (typeQuery.equals("Characters")) {
                            listDetailBookCharacters.add(response.body().getResults());
                            mAdapterCharacters.notifyDataSetChanged();
                        } else if (typeQuery.equals("Teams")) {
                            listDetailBookTeams.add(response.body().getResults());
                            mAdapterTeams.notifyDataSetChanged();
                        } else if (typeQuery.equals("Locations")) {
                            listDetailBookLocations.add(response.body().getResults());
                            mAdapterLocations.notifyDataSetChanged();
                        } else if (typeQuery.equals("Concepts")) {
                            listDetailBookConcepts.add(response.body().getResults());
                            mAdapterConcepts.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel<DetailBookItem>> call, Throwable error) {
                        progressDoalog.dismiss();
                        if (error instanceof SocketTimeoutException) {
                            Toast.makeText(DetailComicActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof IOException) {
                            Toast.makeText(DetailComicActivity.this, "Timeout", Toast.LENGTH_SHORT).show();
                        } else {
                            //Call was cancelled by user
                            if (call.isCanceled()) {
                                Toast.makeText(DetailComicActivity.this, "Call was cancelled forcefully", Toast.LENGTH_SHORT).show();
                            } else {
                                //Generic error handling
                                Toast.makeText(DetailComicActivity.this, "Network Error :: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }

    }

    private void generateDataListCharacters() {

        gridLayoutManager = new GridLayoutManager(this, 2);
        mAdapterCharacters = new RecyclerDetailBookAdapter(listDetailBookCharacters, this, gridLayoutManager);
        recyclerCharacters.setAdapter(mAdapterCharacters);
        recyclerCharacters.setLayoutManager(gridLayoutManager);

    }

    private void generateDataListTeams() {

        gridLayoutManager = new GridLayoutManager(this, 2);
        mAdapterTeams = new RecyclerDetailBookAdapter(listDetailBookTeams, this, gridLayoutManager);
        recyclerTeams.setAdapter(mAdapterTeams);
        recyclerTeams.setLayoutManager(gridLayoutManager);

    }

    private void generateDataListLocations() {

        gridLayoutManager = new GridLayoutManager(this, 2);
        mAdapterLocations = new RecyclerDetailBookAdapter(listDetailBookLocations, this, gridLayoutManager);
        recyclerLocations.setAdapter(mAdapterLocations);
        recyclerLocations.setLayoutManager(gridLayoutManager);

    }

    private void generateDataListConcepts() {

        gridLayoutManager = new GridLayoutManager(this, 2);
        mAdapterConcepts = new RecyclerDetailBookAdapter(listDetailBookConcepts, this, gridLayoutManager);
        recyclerConcepts.setAdapter(mAdapterConcepts);
        recyclerConcepts.setLayoutManager(gridLayoutManager);

    }

}
