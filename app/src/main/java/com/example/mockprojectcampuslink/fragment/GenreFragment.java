package com.example.mockprojectcampuslink.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.activity.MainActivity;
import com.example.mockprojectcampuslink.adapter.ListGenreAdapter;
import com.example.mockprojectcampuslink.listener.OnClickIcon;
import com.example.mockprojectcampuslink.listener.OnClickItem;
import com.example.mockprojectcampuslink.model.Data;
import com.example.mockprojectcampuslink.model.Music;

import java.util.ArrayList;
import java.util.List;

public class GenreFragment extends Fragment {

    private List<Music> mListGenre;
    private RecyclerView mRecyclerViewGenre;
    private OnClickItem onClickGenres = new OnClickItem() {
        @Override
        public void clickItem(int position) {
            Music music = mListGenre.get(position);
            ((MainActivity) getActivity()).listSongOfGenres(music);
        }
    };

    public GenreFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
        initAction();
        addFragmentListGenres();
    }

    private void initAction() {

    }

    private void initView(View view) {
        mRecyclerViewGenre = (RecyclerView) view.findViewById(R.id.recyclerViewGenre);
    }

    private void initData() {
        Data data = new Data(getContext());
        mListGenre = new ArrayList<>();
        mListGenre.addAll(data.getmListGenre());
    }

    private void addFragmentListGenres() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ListGenreAdapter listGenreAdapter = new ListGenreAdapter(mListGenre, onClickGenres, getContext());
        mRecyclerViewGenre.setLayoutManager(gridLayoutManager);
        mRecyclerViewGenre.setAdapter(listGenreAdapter);
    }
}
