package com.example.comicbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comicbook.R
import com.example.comicbook.activities.DetailComicActivity
import com.example.comicbook.models.BookItem
import com.squareup.picasso.Picasso
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RecyclerBookAdapter(private val mList: List<BookItem>?, private val mContext: Context, private val mLayoutManager: GridLayoutManager) : RecyclerView.Adapter<RecyclerBookAdapter.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        val spanCount = mLayoutManager.spanCount
        return if (spanCount == SPAN_COUNT_ONE) {
            VIEW_TYPE_LIST
        } else {
            VIEW_TYPE_GRID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val view: View
        view = if (i == VIEW_TYPE_LIST) {
            LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.book_grid_item, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val bookItemAdapter = mList!![position]
        Picasso.get()
                .load(bookItemAdapter.image!!.original_url)
                .resize(350, 500)
                .centerCrop()
                .into((viewHolder as ViewHolder?)!!.image_original_url)
        viewHolder!!.name_issue_number.text = bookItemAdapter.name + " #" + bookItemAdapter.issue_number
        try {
            var pattern = "yyyy-mm-dd HH:mm:ss"
            var simpleDateFormat = SimpleDateFormat(pattern)
            val locale = Locale("en")
            val dateFormatSymbols = DateFormatSymbols(locale)
            val date = simpleDateFormat.parse(bookItemAdapter.date_added)
            pattern = "MMMM dd,yyyy"
            simpleDateFormat = SimpleDateFormat(pattern, dateFormatSymbols)
            val date_added = simpleDateFormat.format(date)
            viewHolder!!.date_added.text = date_added
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        viewHolder!!.itemView.setOnClickListener {
            val intent = Intent(mContext, DetailComicActivity::class.java)
            intent.putExtra("original_url_book", bookItemAdapter.image!!.original_url)
            intent.putExtra("api_detail_url", bookItemAdapter.api_detail_url!!.substring(35))
            mContext.startActivity(intent)
        }
        viewHolder.itemView.setOnLongClickListener {
            //Toast.makeText(v.getContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
//return true;
            val intent = Intent(mContext, DetailComicActivity::class.java)
            intent.putExtra("original_url_book", bookItemAdapter.image!!.original_url)
            intent.putExtra("api_detail_url", bookItemAdapter.api_detail_url)
            mContext.startActivity(intent)
            true
        }
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image_original_url: ImageView
        var name_issue_number: TextView
        var date_added: TextView

        init {
            image_original_url = itemView.findViewById(R.id.image_original_url)
            name_issue_number = itemView.findViewById(R.id.name_issue_number)
            date_added = itemView.findViewById(R.id.date_added)
        }
    }

    companion object {
        const val SPAN_COUNT_ONE = 1
        const val SPAN_COUNT_THREE = 3
        private const val VIEW_TYPE_GRID = 1
        private const val VIEW_TYPE_LIST = 2
    }

}