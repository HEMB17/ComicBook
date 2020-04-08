package com.example.comicbook.activities

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comicbook.R
import com.example.comicbook.activities.MainActivity
import com.example.comicbook.adapters.RecyclerBookAdapter
import com.example.comicbook.models.BookItem
import com.example.comicbook.models.ResultModel
import com.example.comicbook.services.GetDataService
import com.example.comicbook.services.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var recyclerBook: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    var mList: List<BookItem> = ArrayList()
    var progressDoalog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isNetworkAvailable(this)) {
            progressDoalog = ProgressDialog(this@MainActivity)
            progressDoalog!!.setMessage("Loading....")
            progressDoalog!!.show()
            val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
            val call = service.allBooks
            call.enqueue(object : Callback<ResultModel<List<BookItem>>> {
                override fun onResponse(call: Call<ResultModel<List<BookItem>>>, response: Response<ResultModel<List<BookItem>>>) {
                    progressDoalog!!.dismiss()
                    generateDataList(response.body()!!.results)
                }

                override fun onFailure(call: Call<ResultModel<List<BookItem>>>, error: Throwable) {
                    progressDoalog!!.dismiss()
                    if (error is SocketTimeoutException) {
                        Toast.makeText(this@MainActivity, "Connection Timeout", Toast.LENGTH_SHORT).show()
                    } else if (error is IOException) {
                        Toast.makeText(this@MainActivity, "Timeout", Toast.LENGTH_SHORT).show()
                    } else { //Call was cancelled by user
                        if (call.isCanceled) {
                            Toast.makeText(this@MainActivity, "Call was cancelled forcefully", Toast.LENGTH_SHORT).show()
                        } else { //Generic error handling
                            Toast.makeText(this@MainActivity, "Network Error :: " + error.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        } else {
            Toast.makeText(this@MainActivity, "Not Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateDataList(mList: List<BookItem>) {
        gridLayoutManager = GridLayoutManager(this, RecyclerBookAdapter.SPAN_COUNT_ONE)
        recyclerBook = findViewById(R.id.recyclerBooks)
        mAdapter = RecyclerBookAdapter(mList, this, gridLayoutManager!!)
        recyclerBook?.adapter = mAdapter
        recyclerBook?.layoutManager = gridLayoutManager
    }

    /**
     * private List<BookItem> getBookItem(){
     *
     * ArrayList<BookItem> mList = new ArrayList<>();
     * mList.add(new BookItem("https://www.definicionabc.com/wp-content/uploads/Comic.jpg","Comic Book Name", "3", new Date()));
     * mList.add(new BookItem("","Comic Book Name", "3", new Date()));
     * mList.add(new BookItem("https://www.definicionabc.com/wp-content/uploads/Comic.jpg","Comic Book Name", "3", new Date()));
     * mList.add(new BookItem("","Comic Book Name", "3", new Date()));
     * return mList;
     *
     * }
    </BookItem></BookItem> */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_switch_layout) {
            switchLayout()
            switchIcon(item)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun switchLayout() {
        if (gridLayoutManager!!.spanCount == RecyclerBookAdapter.SPAN_COUNT_ONE) {
            gridLayoutManager!!.spanCount = RecyclerBookAdapter.SPAN_COUNT_THREE
        } else {
            gridLayoutManager!!.spanCount = RecyclerBookAdapter.SPAN_COUNT_ONE
        }
        //RecyclerBookAdapter.notifyItemRangeChanged(0, RecyclerBookAdapter.getItemCount());
    }

    private fun switchIcon(item: MenuItem) {
        if (gridLayoutManager!!.spanCount == RecyclerBookAdapter.SPAN_COUNT_THREE) {
            item.icon = resources.getDrawable(R.drawable.ic_action_grid)
        } else {
            item.icon = resources.getDrawable(R.drawable.ic_action_list)
        }
    }

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            var connected = false
            val connec = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // Recupera todas las redes (tanto móviles como wifi)
            val redes = connec.allNetworkInfo
            for (i in redes.indices) { // Si alguna red tiene conexión, se devuelve true
                if (redes[i].state == NetworkInfo.State.CONNECTED) {
                    connected = true
                }
            }
            return connected
        }
    }
}