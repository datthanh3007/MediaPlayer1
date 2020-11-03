package com.example.mockprojectcampuslink.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.activity.MainActivity;
import com.example.mockprojectcampuslink.adapter.ListSongOfAuthorAdapter;
import com.example.mockprojectcampuslink.listener.OnClickIcon;
import com.example.mockprojectcampuslink.model.Data;
import com.example.mockprojectcampuslink.model.Music;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class ListSongOfAuthorFragment extends Fragment {

    private static final String TAG = "SongOfAuthor";
    private ArrayList<Music> arrayList, mList;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mImageView;
    private FloatingActionButton mFloatingActionButton;
    private Button mButtonRandom;
    private RecyclerView mRecyclerView;
    private Music mMusicAuthor, mMusicGenres;
    private OnClickIcon onClickIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_song_of_author, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
        setItemView();
        initAction();
        addFragment();
    }

    private void initAction() {
        onClickIcon = new OnClickIcon() {
            @Override
            public void clickIcon(int position) {

            }

            @Override
            public void clickSong(int position) {
                Music music = mList.get(position);
                ((MainActivity) getActivity()).data(music, mList, position);
                ListSongOfAuthorFragment listSongOfAuthorFragment = (ListSongOfAuthorFragment) getFragmentManager().findFragmentByTag("danhsach");
                if (listSongOfAuthorFragment != null)
                    getFragmentManager().popBackStack("danhsach", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        };
    }

    private void setItemView() {
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListSongOfAuthorFragment listSongOfAuthorFragment = (ListSongOfAuthorFragment) getFragmentManager().findFragmentByTag("danhsach");
                if (listSongOfAuthorFragment != null) {
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(0, R.anim.slide_in_down)
                            .remove(listSongOfAuthorFragment)
                            .commit();
                    getFragmentManager().popBackStack();
                }
            }
        });
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        if (mMusicAuthor != null) {
            mImageView.setImageBitmap(mMusicAuthor.getBitmap());
            mCollapsingToolbarLayout.setTitle(mMusicAuthor.getAuthor());

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mMusicAuthor.getBitmap());
            mCollapsingToolbarLayout.setBackground(bitmapDrawable);
            mCollapsingToolbarLayout.post(new Runnable() {
                @Override
                public void run() {
                    Blurry.with(getContext()).onto(mCollapsingToolbarLayout);
                }
            });
//            Blurry.with(getContext()).from(music.getBitmap()).into(mImageView);
        }
        if (mMusicGenres != null) {
            mImageView.setImageBitmap(mMusicGenres.getBitmap());
            mCollapsingToolbarLayout.setTitle(mMusicGenres.getGenres());

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mMusicGenres.getBitmap());
            mCollapsingToolbarLayout.setBackground(bitmapDrawable);
            mCollapsingToolbarLayout.post(new Runnable() {
                @Override
                public void run() {
                    Blurry.with(getContext()).onto(mCollapsingToolbarLayout);
                }
            });
        }
    }

    private void addFragment() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ListSongOfAuthorAdapter listSongOfAuthorAdapter = new ListSongOfAuthorAdapter(getContext(), mList, onClickIcon);
        mRecyclerView.setAdapter(listSongOfAuthorAdapter);
    }

    private void initView(View view) {
        mAppBarLayout = (AppBarLayout) view.findViewById(R.id.appBarLayout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolBar);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.btnFloating);
        /* mButtonRandom = view.findViewById(R.id.btnRandom); */
        mToolbar = (Toolbar) view.findViewById(R.id.toolBar);
        mImageView = (ImageView) view.findViewById(R.id.imgTest);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listBaiHat);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMusicAuthor = bundle.getParcelable("songOfAuthor");
            mMusicGenres = bundle.getParcelable("songOfGenres");

            Data data = new Data(getContext());
            mList = new ArrayList<>();
            mList.addAll(data.getMusic());
            arrayList = new ArrayList<>();

            if (mMusicAuthor != null) {
                for (Music music1 : mList) {
                    if (mMusicAuthor.getAuthor().equals(music1.getAuthor())) {
                        arrayList.add(music1);
                    }
                }
            }

            if (mMusicGenres != null) {
                for (Music music1 : mList) {
                    if (mMusicGenres.getGenres().equals(music1.getGenres())) {
                        arrayList.add(music1);
                    }
                }
            }

            mList.clear();
            mList.addAll(arrayList);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
