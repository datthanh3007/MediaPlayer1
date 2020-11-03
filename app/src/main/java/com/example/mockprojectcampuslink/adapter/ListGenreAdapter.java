package com.example.mockprojectcampuslink.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.listener.OnClickItem;
import com.example.mockprojectcampuslink.model.Music;

import java.util.List;

public class ListGenreAdapter extends RecyclerView.Adapter<ListGenreAdapter.ViewHolder> {

    private List<Music> mList;
    private Context mContext;
    private OnClickItem onClickItem;

    public ListGenreAdapter(List<Music> music, OnClickItem onClick, Context context){
        this.mList = music;
        this.mContext = context;
        this.onClickItem = onClick;
    }


    @NonNull
    @Override
    public ListGenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_genre, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListGenreAdapter.ViewHolder viewHolder, int i) {
        Music music = mList.get(i);

        viewHolder.textViewGenre.setText(music.getGenres());
        viewHolder.imageView.setImageBitmap(music.getBitmap());
//        Glide.with(mContext).load("https://cdn-images.saostar.vn/wp500/2019/07/07/5563483/66626634_2375876072500384_2348050868307230720_n.jpg")
//                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewGenre;
        ImageView imageView;

        View.OnClickListener clickGenres = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.clickItem(getAdapterPosition());
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewGenre = (TextView) itemView.findViewById(R.id.txtGenre);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(clickGenres);
        }
    }
}
