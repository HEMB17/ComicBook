package com.example.comicbook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.comicbook.R;
import com.example.comicbook.adapters.RecyclerBookAdapter;
import com.example.comicbook.models.BookItem;
import com.example.comicbook.models.ResultModel;
import com.example.comicbook.services.GetDataService;
import com.example.comicbook.services.RetrofitClientInstance;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.comicbook.adapters.RecyclerBookAdapter.SPAN_COUNT_ONE;
import static com.example.comicbook.adapters.RecyclerBookAdapter.SPAN_COUNT_THREE;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerBook;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    List<BookItem> mList = new ArrayList<>();
    ProgressDialog progressDoalog;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int offset = 0;
    List<BookItem> listBook = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerBook = findViewById(R.id.recyclerBooks);


        if(isNetworkAvailable(this)) {

            getBook(offset, 10);
            generateDataList();

        }else{
            Toast.makeText(MainActivity.this, "Not Internet Connection", Toast.LENGTH_SHORT).show();
        }

        recyclerBook.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if( loading){
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            offset += 10;

                            getBook(offset  , 10);
                            //Toast.makeText(MainActivity.this, "Last Item Wow !", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            }
        });


    }

    private void generateDataList() {

        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_ONE);
        mAdapter = new RecyclerBookAdapter(listBook, this, gridLayoutManager);
        recyclerBook.setAdapter(mAdapter);
        recyclerBook.setLayoutManager(gridLayoutManager);

    }

    public void getBook(int offset, int limit){

        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResultModel<List<BookItem>>> call = service.getAllBooks("1ed7967e84b13642e18f74eae0c8e399d0ddbe9b",offset,limit);
        call.enqueue(new Callback<ResultModel<List<BookItem>>>() {
            @Override
            public void onResponse(Call<ResultModel<List<BookItem>>> call, Response<ResultModel<List<BookItem>>> response) {

                progressDoalog.dismiss();

                for(BookItem unListBook : response.body().getResults()){
                    listBook.add(unListBook);
                }


                mAdapter.notifyDataSetChanged();
                loading = true;

            }

            @Override
            public void onFailure(Call<ResultModel<List<BookItem>>> call, Throwable error) {
                progressDoalog.dismiss();
                if (error instanceof SocketTimeoutException)
                {
                    Toast.makeText(MainActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof IOException)
                {
                    Toast.makeText(MainActivity.this, "Timeout", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Call was cancelled by user
                    if(call.isCanceled())
                    {
                        Toast.makeText(MainActivity.this, "Call was cancelled forcefully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Generic error handling
                        Toast.makeText(MainActivity.this, "Network Error :: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static boolean isNetworkAvailable(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;

    }
/**
    private List<BookItem> getBookItem(){

        ArrayList<BookItem> mList = new ArrayList<>();
        mList.add(new BookItem("https://www.definicionabc.com/wp-content/uploads/Comic.jpg","Comic Book Name", "3", new Date()));
        mList.add(new BookItem("","Comic Book Name", "3", new Date()));
        mList.add(new BookItem("https://www.definicionabc.com/wp-content/uploads/Comic.jpg","Comic Book Name", "3", new Date()));
        mList.add(new BookItem("","Comic Book Name", "3", new Date()));
        return mList;

    }
**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_switch_layout) {
            switchLayout();
            switchIcon(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchLayout() {
        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_ONE) {
            gridLayoutManager.setSpanCount(SPAN_COUNT_THREE);
        } else {
            gridLayoutManager.setSpanCount(SPAN_COUNT_ONE);
        }
        //RecyclerBookAdapter.notifyItemRangeChanged(0, RecyclerBookAdapter.getItemCount());
    }

    private void switchIcon(MenuItem item) {
        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_THREE) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_grid));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_list));
        }
    }

}
