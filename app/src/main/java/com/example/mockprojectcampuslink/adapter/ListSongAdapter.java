package com.example.mockprojectcampuslink.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.listener.OnClickIcon;
import com.example.mockprojectcampuslink.model.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListSongAdapter extends RecyclerView.Adapter<ListSongAdapter.ViewHolder> {

    List<Music> list;
    ArrayList<Music> arrayList;
    private Context mContext;
    private OnClickIcon mOnClickItemSong;

    public ListSongAdapter(Context context, List<Music> list, OnClickIcon onClickItemSong){
        this.list = list;
        this.mContext = context;
        this.mOnClickItemSong = onClickItemSong;
        this.arrayList = new ArrayList<>();
        arrayList.addAll(list);
    }

    @NonNull
    @Override
    public ListSongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_song, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSongAdapter.ViewHolder viewHolder, int i) {
        Music music = list.get(i);

        viewHolder.textViewSong.setText(music.getSong());
        viewHolder.textViewAuhor.setText(music.getAuthor());
        viewHolder.imageView.setImageBitmap(music.getBitmap());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSong, textViewAuhor;
        ImageButton imageButton;
        ImageView imageView;
        LinearLayout linearLayout;

        View.OnClickListener clickIcon = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickItemSong.clickIcon(getAdapterPosition());
            }
        };

        View.OnClickListener clickSong = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickItemSong.clickSong(getAdapterPosition());
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageButton = (ImageButton) itemView.findViewById(R.id.iBtnMoreVertical);
            textViewAuhor = (TextView) itemView.findViewById(R.id.txtAuthorName);
            textViewSong = (TextView) itemView.findViewById(R.id.txtSongName);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewTest);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearListSong);
            linearLayout.setOnClickListener(clickSong);
            imageButton.setOnClickListener(clickIcon);
        }
    }

    public void search(String text) {
        text = text.toLowerCase(Locale.getDefault());
        list.clear();
        if (text.length() == 0) {
            list.addAll(arrayList);
        } else {
            for (Music music : arrayList) {
                if (music.getSong().toLowerCase(Locale.getDefault()).contains(text)) list.add(music);
            }
        }
        notifyDataSetChanged();
    }
}
