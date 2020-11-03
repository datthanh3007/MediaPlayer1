package com.example.mockprojectcampuslink.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.listener.OnClickItem;
import com.example.mockprojectcampuslink.model.Music;

import java.util.ArrayList;
import java.util.List;

public class ListAuthorAdapter extends RecyclerView.Adapter<ListAuthorAdapter.ViewHolder> {

    private List<Music> mList;
    private ArrayList<Integer> arrayList;
    private Context mContext;
    private OnClickItem mOnClickItem;

    public ListAuthorAdapter(Context context, List<Music> list, OnClickItem onClickItem, ArrayList<Integer> integerArrayList){
        this.mList = list;
        this.mOnClickItem = onClickItem;
        this.mContext = context;
        this.arrayList = integerArrayList;
    }

    @NonNull
    @Override
    public ListAuthorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_author, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAuthorAdapter.ViewHolder viewHolder, int i) {
        Music music = mList.get(i);
        int countSong = arrayList.get(i);

        viewHolder.textViewAuthor.setText(music.getAuthor());
        viewHolder.textViewSongAmount.setText(Integer.toString(countSong));
        viewHolder.imageView.setImageBitmap(music.getBitmap());
//        Glide.with(mContext).load("https://cdn-images.saostar.vn/wp500/2019/07/07/5563483/66626634_2375876072500384_2348050868307230720_n.jpg")
//                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAuthor, textViewSongAmount;
        ImageView imageView;

        View.OnClickListener clickAuthor = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickItem.clickItem(getAdapterPosition());
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSongAmount = (TextView) itemView.findViewById(R.id.txtSongAmount);
            textViewAuthor = (TextView) itemView.findViewById(R.id.txtAuthorName);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewTest);
            itemView.setOnClickListener(clickAuthor);
        }
    }
}
