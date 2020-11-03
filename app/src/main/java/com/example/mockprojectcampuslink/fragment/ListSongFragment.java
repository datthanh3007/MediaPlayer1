package com.example.mockprojectcampuslink.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.activity.MainActivity;
import com.example.mockprojectcampuslink.adapter.ListSongAdapter;
import com.example.mockprojectcampuslink.adapter.MusicPagerAdapter;
import com.example.mockprojectcampuslink.listener.OnClickIcon;
import com.example.mockprojectcampuslink.listener.updateData;

import com.example.mockprojectcampuslink.model.Data;
import com.example.mockprojectcampuslink.model.Music;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class ListSongFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, updateData {

    private ArrayList<Music> mMyMusic;
    private ArrayList<Music> mListSong;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private TextView mTxtSort;
    private ImageButton mImageButtonSort;
    private Button mButtonRandom;
    private OnClickIcon mOnClickItemSong;
    private ListSongAdapter listSongAdapter;
    private RelativeLayout relativeLayout;
    private Comparator<Music> comparator;
    private String textSearch;
    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs" ;

    public ListSongFragment() {
    }

    public static ListSongFragment newInstance() {

        Bundle args = new Bundle();

        ListSongFragment fragment = new ListSongFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                setupAdapter();
                mSwipeRefreshLayout.setRefreshing(false);
                ((MainActivity) getActivity()).dataChange("update");
            }
        }, 2000);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_song, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();

        initAction();
        refreshData();

        dataRecyclerView();

    }

    private void initView(View view) {
        relativeLayout = (RelativeLayout) view.findViewById(R.id.testSort);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSong);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mButtonRandom = (Button) view.findViewById(R.id.btnRandom);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mImageButtonSort = (ImageButton) view.findViewById(R.id.imgButtonSort);
        mTxtSort = (TextView) view.findViewById(R.id.txtSort);
    }

    private void initData() {
        Data data = new Data(getContext());

        mMyMusic = new ArrayList<>();
        mMyMusic.addAll(data.getMusic());

        mListSong = new ArrayList<>();
        mListSong.addAll(mMyMusic);
    }

    private void dataRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        listSongAdapter = new ListSongAdapter(getContext(), mListSong, mOnClickItemSong);
        mRecyclerView.setAdapter(listSongAdapter);
    }

    private void initAction() {
        mOnClickItemSong = new OnClickIcon() {
            @Override
            public void clickIcon(int position) {
                Music music = mListSong.get(position);
                SettingsFragment settingsFragment = SettingsFragment.newInstance(music);
                settingsFragment.setCancelable(true);
                settingsFragment.show(getFragmentManager(), null);
            }

            @Override
            public void clickSong(int position) {
                Music music = mListSong.get(position);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.data(music, mListSong, position);
            }
        };

        mImageButtonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortFragment sortFragment = new SortFragment();
                sortFragment.setCancelable(true);
                sortFragment.show(getFragmentManager(), "sort");
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch = s;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch = s;
                listSongAdapter.search(textSearch);
                return false;
            }
        });

        mButtonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int position = random.nextInt(mListSong.size());
                Music music = mListSong.get(position);
                ((MainActivity) getActivity()).data(music, mListSong, position);
            }
        });
    }

    private void setupAdapter() {
        ListSongAdapter listSongAdapter = new ListSongAdapter(getContext(), mListSong, mOnClickItemSong);
        mRecyclerView.setAdapter(listSongAdapter);
    }

    private void refreshData() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.black_color);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void update() {
//            SharedPreferences myPreferences = getActivity().getSharedPreferences(MY_SHARED_PREFERENCES , MODE_PRIVATE);
//            author = myPreferences.getString("author", null);
        mListSong.clear();
        mListSong.addAll(mMyMusic);
    }

    @Override
    public void sortSongAZ() {
        comparator = new Comparator<Music>() {
            @Override
            public int compare(Music music, Music t1) {
                return music.getSong().compareTo(t1.getSong());
            }
        };
        Collections.sort(mListSong, comparator);
    }


    @Override
    public void sortSongZA() {
        comparator = new Comparator<Music>() {
            @Override
            public int compare(Music music, Music t1) {
                return t1.getSong().compareTo(music.getSong());
            }
        };
        Collections.sort(mListSong, comparator);
    }
//    private void addToPlaylist(String playlistName, int songID) {
//
//        //Vibrate device
//        Utils.vibrate(getApplicationContext());
//
//        //get all playlists
//        Cursor playListCursor = AppController.getGlobalContentResolvere().query(
//                MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, new String[]{"*"}, null, null,
//                null);
//
//        long playlistId = 0;
//
//        playListCursor.moveToFirst();
//
//        do {
//
//            //check if selected playlsit already exist
//            if (playListCursor.getString(playListCursor
//                    .getColumnIndex(MediaStore.Audio.Playlists.NAME)).
//                    equalsIgnoreCase(playlistName)) {
//
//                playlistId = playListCursor.getLong(playListCursor
//                        .getColumnIndex(MediaStore.Audio.Playlists._ID));
//                break;
//            }
//        } while (playListCursor.moveToNext());
//
//        //Playlist  doesnt exist creating new with given name
//        if (playlistId == 0) {
//
//            Log.d(TAG, "CREATING PLAYLIST: " + playlistName);
//
//            ContentValues playlisrContentValue = new ContentValues();
//
//            //Add name
//            playlisrContentValue.put(MediaStore.Audio.Playlists.NAME, playlistName);
//
//            //update modified value
//            playlisrContentValue.put(MediaStore.Audio.Playlists.DATE_MODIFIED,
//                    System.currentTimeMillis());
//
//            Uri playlistURl = AppController.getGlobalContentResolvere().insert(
//                    MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, playlisrContentValue);
//
//            Log.d(TAG, "Added PlayLIst: " + playlistURl);
//
//        } else {
//
//            //Playlist alreay exist add to playlist
//            String[] cols = new String[]{
//                    "count(*)"
//            };
//
//            Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
//
//            Cursor favListCursor = AppController.getGlobalContentResolvere().query(uri, cols, null, null, null);
//
//            favListCursor.moveToFirst();
//
//            final int base = favListCursor.getInt(0);
//
//            //playlist updated delete older playlist art so that we can create new
//            Toast.makeText(AudioPlayerActivity.this, "deleted old file" + new File(AppContants.PLAY_LIST_DIR + playlistId + ".png").delete(), Toast.LENGTH_SHORT).show();
//
//            favListCursor.close();
//
//            //add song to last
//            ContentValues values = new ContentValues();
//
//            values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, base + songID);
//
//            values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, songID);
//
//            AppController.getGlobalContentResolvere().insert(uri, values);
//
//
//            //Debug purpose
//            Toast.makeText(AudioPlayerActivity.this, "Added to Favourite fragment_list_song_of_author " +
//                            CenterRepository.getInstance().getAudioCollection().getSongAt(AppConfig.SONG_NUMBER).getTitle()
//                    , Toast.LENGTH_SHORT).show();
//
//        }
//    }
}
