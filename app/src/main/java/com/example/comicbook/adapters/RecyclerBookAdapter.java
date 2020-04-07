package com.example.comicbook.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicbook.R;
import com.example.comicbook.activities.DetailComicActivity;
import com.example.comicbook.models.BookItem;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerBookAdapter extends RecyclerView.Adapter {

    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;

    private static final int VIEW_TYPE_GRID = 1;
    private static final int VIEW_TYPE_LIST = 2;

    private List<BookItem> mList;
    private Context mContext;
    private GridLayoutManager mLayoutManager;

    public RecyclerBookAdapter(List<BookItem> list, Context context, GridLayoutManager layoutManager){
        super();
        mList = list;
        mContext = context;
        mLayoutManager = layoutManager;
    }

    @Override
    public int getItemViewType(int position) {
        int spanCount = mLayoutManager.getSpanCount();
        if (spanCount == SPAN_COUNT_ONE) {
            return VIEW_TYPE_LIST;
        } else {
            return VIEW_TYPE_GRID;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view;
        if (i == VIEW_TYPE_LIST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_grid_item, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        BookItem bookItemAdapter = mList.get(position);

        Picasso.get()
                .load(bookItemAdapter.getImage().getOriginal_url())
                .resize(350, 500)
                .centerCrop()
                .into(((ViewHolder) viewHolder).image_original_url);

        ((ViewHolder) viewHolder).name_issue_number.setText(bookItemAdapter.getName() + " #" + bookItemAdapter.getIssue_number());

        try {

            String pattern = "yyyy-mm-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Locale locale = new Locale("en");
            DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
            Date date = simpleDateFormat.parse(bookItemAdapter.getDate_added());

            pattern = "MMMM dd,yyyy";
            simpleDateFormat = new SimpleDateFormat(pattern, dateFormatSymbols);
            String date_added = simpleDateFormat.format(date);
            ((ViewHolder) viewHolder).date_added.setText(date_added);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailComicActivity.class);
                intent.putExtra("original_url_book", bookItemAdapter.getImage().getOriginal_url());
                intent.putExtra("api_detail_url", bookItemAdapter.getApi_detail_url().substring(35));
                mContext.startActivity(intent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(v.getContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
                //return true;
                Intent intent = new Intent(mContext, DetailComicActivity.class);
                intent.putExtra("original_url_book", bookItemAdapter.getImage().getOriginal_url());
                intent.putExtra("api_detail_url", bookItemAdapter.getApi_detail_url());
                mContext.startActivity(intent);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image_original_url;
        public TextView name_issue_number;
        public TextView date_added;
        public ViewHolder(View itemView) {
            super(itemView);
            image_original_url = itemView.findViewById(R.id.image_original_url);
            name_issue_number = itemView.findViewById(R.id.name_issue_number);
            date_added = itemView.findViewById(R.id.date_added);
        }
    }

}
