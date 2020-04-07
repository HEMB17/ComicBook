package com.example.comicbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicbook.R;
import com.example.comicbook.models.DetailBookItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerDetailBookAdapter extends RecyclerView.Adapter {

    private List<DetailBookItem> mList;
    private Context mContext;
    private GridLayoutManager mLayoutManager;

    public RecyclerDetailBookAdapter(List<DetailBookItem> list, Context context, GridLayoutManager layoutManager){
        super();
        mList = list;
        mContext = context;
        mLayoutManager = layoutManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_book_grid_item, parent, false);
        RecyclerDetailBookAdapter.ViewHolder viewHolder = new RecyclerDetailBookAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DetailBookItem detailBookItem = mList.get(position);
        ((RecyclerDetailBookAdapter.ViewHolder) viewHolder).name.setText(detailBookItem.getName());
        Picasso.get()
                .load(detailBookItem.getImage().getOriginal_url())
                    .resize(100, 100)
                .centerCrop()
                .into(((RecyclerDetailBookAdapter.ViewHolder) viewHolder).image_original_url);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView image_original_url;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image_original_url = itemView.findViewById(R.id.image_original_url);
        }
    }


}
