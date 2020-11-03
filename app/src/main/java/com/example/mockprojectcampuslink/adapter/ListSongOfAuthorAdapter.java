package com.example.mockprojectcampuslink.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.fragment.ListSongOfAuthorFragment;
import com.example.mockprojectcampuslink.listener.OnClickIcon;
import com.example.mockprojectcampuslink.model.Music;

import java.util.List;

public class ListSongOfAuthorAdapter extends RecyclerView.Adapter<ListSongOfAuthorAdapter.ViewHolder> {

    private List<Music> mList;
    private Context mContext;
    OnClickIcon mOnClickIcon;

    public ListSongOfAuthorAdapter(Context context, List<Music> list, OnClickIcon onClickIcon) {
        mContext = context;
        mList = list;
        this.mOnClickIcon = onClickIcon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_song_of_author, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Music music = mList.get(i);

        viewHolder.txtSongName.setText(music.getSong());
        viewHolder.txtCountSong.setText(i + 1 + "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCountSong, txtSongName;
        LinearLayout linearLayout;

        View.OnClickListener clickSong = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickIcon.clickSong(getAdapterPosition());
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSongName = (TextView) itemView.findViewById(R.id.listSongName);
            txtCountSong = (TextView) itemView.findViewById(R.id.txtCountSong);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.llSongOfAuthor);
            linearLayout.setOnClickListener(clickSong);
        }
    }
}
