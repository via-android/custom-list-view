package com.morepranit.customlistviewdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.morepranit.customlistviewdemo.AppController;
import com.morepranit.customlistviewdemo.models.BookModel;
import com.morepranit.customlistviewdemo.R;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {
    Context context;
    ArrayList<BookModel> bookModels;
    ImageLoader loader;

    public BookAdapter(Context context, ArrayList<BookModel> bookModels) {
        this.context = context;
        this.bookModels = bookModels;
        loader = AppController.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        return bookModels.size();
    }

    @Override
    public Object getItem(int i) {
        return bookModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.ivBook = view.findViewById(R.id.iv_book);
            holder.tvAuthors = view.findViewById(R.id.tv_authors);
            holder.tvTitle = view.findViewById(R.id.tv_title);
            holder.tvDesc = view.findViewById(R.id.tv_desc);
            holder.tvPubDate = view.findViewById(R.id.tv_pub_date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        BookModel model = bookModels.get(i);
        holder.tvTitle.setText(model.getTitle());
        holder.tvDesc.setText(
                model.getDescription().length() > 30 ?
                model.getDescription().substring(0, 30) + "..." :
                model.getDescription()
        );
        holder.tvPubDate.setText(model.getPublishedDate());
        holder.tvAuthors.setText(model.getAuthors().toString());

        holder.ivBook.setImageUrl(model.getThumbnail(), loader);

        return view;
    }

    class ViewHolder {
        NetworkImageView ivBook;
        TextView tvTitle, tvDesc, tvAuthors, tvPubDate;
    }
}
