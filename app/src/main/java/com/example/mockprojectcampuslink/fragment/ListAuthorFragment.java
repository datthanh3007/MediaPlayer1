package com.example.mockprojectcampuslink.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.activity.MainActivity;
import com.example.mockprojectcampuslink.adapter.ListAuthorAdapter;
import com.example.mockprojectcampuslink.listener.OnClickItem;
import com.example.mockprojectcampuslink.model.Data;
import com.example.mockprojectcampuslink.model.Music;

import java.util.ArrayList;
import java.util.List;

public class ListAuthorFragment extends Fragment {

    SharedPreferences preferences;
    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs" ;

    private List<Music> mListAuthor;
    private ArrayList<Integer> listCountSong;
    private RecyclerView mRecyclerView;
    private OnClickItem mOnClickItem = new OnClickItem() {
        @Override
        public void clickItem(int position) {
            Music music = mListAuthor.get(position);
//            preferences = getActivity().getSharedPreferences(MY_SHARED_PREFERENCES, getContext().MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("author", author);
//            editor.commit();
            ((MainActivity) getActivity()).listSongOfAuthor(music);
        }
    };

    public ListAuthorFragment(){}

    public static ListAuthorFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ListAuthorFragment fragment = new ListAuthorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_author, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
        addFragmentListAuthor();
    }

    public void initData() {
        Data data = new Data(getContext());
        mListAuthor = new ArrayList<>();
        listCountSong = new ArrayList<>();
        mListAuthor.addAll(data.getmListAuthor());
        listCountSong.addAll(data.getCountSong());
    }

    public void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewAuthor);
    }

    public void addFragmentListAuthor() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ListAuthorAdapter listAuthorAdapter = new ListAuthorAdapter(getContext(), mListAuthor, mOnClickItem, listCountSong);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        listAuthorAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(listAuthorAdapter);
    }
}
