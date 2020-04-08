package com.example.comicbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comicbook.R
import com.example.comicbook.models.DetailBookItem
import com.squareup.picasso.Picasso

class RecyclerDetailBookAdapter(private val mList: List<DetailBookItem>, private val mContext: Context, private val mLayoutManager: GridLayoutManager) : RecyclerView.Adapter<RecyclerDetailBookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_book_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val detailBookItem = mList[position]
        (viewHolder as ViewHolder?)!!.name.text = detailBookItem.name
        Picasso.get()
                .load(detailBookItem.image!!.original_url)
                .resize(100, 100)
                .centerCrop()
                .into(viewHolder!!.image_original_url)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var image_original_url: ImageView

        init {
            name = itemView.findViewById(R.id.name)
            image_original_url = itemView.findViewById(R.id.image_original_url)
        }
    }

}