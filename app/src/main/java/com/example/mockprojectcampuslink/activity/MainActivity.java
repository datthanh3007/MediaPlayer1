package com.example.mockprojectcampuslink.activity;

import android.Manifest;
import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.fragment.FullMusicControlFragment;
import com.example.mockprojectcampuslink.adapter.MusicPagerAdapter;
import com.example.mockprojectcampuslink.fragment.ListSongOfAuthorFragment;
import com.example.mockprojectcampuslink.fragment.MusicControlFragment;
import com.example.mockprojectcampuslink.fragment.SettingsFragment;
import com.example.mockprojectcampuslink.listener.OnBackPressFragment;
import com.example.mockprojectcampuslink.listener.OnListenerControl;
import com.example.mockprojectcampuslink.model.Music;
import com.example.mockprojectcampuslink.service.MusicControl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TextView mTxtAppName;

    private OnListenerControl mOnListenerControl;
    private MusicControlFragment mMusicControlFragment;
    private FullMusicControlFragment mFullMusicControlFragment;

    private ServiceConnection mServiceConnection;
    private MusicControl mMusicControl;
    private boolean mIsConnected = false;
    private String path;
    private Bundle bundle;
    public FrameLayout frameLayout;
    MusicPagerAdapter adapter = new MusicPagerAdapter(this, getSupportFragmentManager());

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            return;
        }

        initView();
        addViewPagerMusic();
        initAction();
    }

    public void data(Music music, ArrayList<Music> arrayList, int i) {
        if (music != null) {
            path = music.getPath();
            bundle = new Bundle();
            bundle.putParcelable("img", music);
            bundle.putInt("i", i);
            bundle.putParcelableArrayList("fragment_list_song_of_author", arrayList);

            if (mServiceConnection == null) {
                connectedService();
            } else {
                mMusicControl.stopMusic();
                unbindService(mServiceConnection);
                connectedService();
                MusicControlFragment musicControlFragment = (MusicControlFragment) getSupportFragmentManager().findFragmentByTag("musiccontrol");
                if (musicControlFragment != null) {
                    Bundle bundleReload = new Bundle();
                    bundleReload.putParcelable("img", music);
                    bundleReload.putInt("i", i);
                    bundleReload.putParcelableArrayList("fragment_list_song_of_author", arrayList);
                    musicControlFragment.getArguments().putAll(bundleReload);
                    getSupportFragmentManager().beginTransaction()
                            .detach(musicControlFragment)
                            .attach(musicControlFragment)
                            .commit();
                }
                FullMusicControlFragment fullMusicControlFragment = (FullMusicControlFragment) getSupportFragmentManager().findFragmentByTag("fullmusiccontrol");
                if (fullMusicControlFragment != null) {
                    getSupportFragmentManager().popBackStack("full", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                ListSongOfAuthorFragment listSongOfAuthorFragment = (ListSongOfAuthorFragment) getSupportFragmentManager().findFragmentByTag("danhsach");
                if (listSongOfAuthorFragment != null)
                    getSupportFragmentManager().popBackStack("danhsach", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public void listSongOfAuthor(Music music) {
        ListSongOfAuthorFragment listSongOfAuthorFragment = new ListSongOfAuthorFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("songOfAuthor", music);
        listSongOfAuthorFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, 0)
                .add(R.id.fullMusicControl, listSongOfAuthorFragment, "danhsach")
                .addToBackStack("danhsach")
                .commit();
    }

    public void listSongOfGenres(Music music) {
        ListSongOfAuthorFragment listSongOfAuthorFragment = new ListSongOfAuthorFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("songOfGenres", music);
        listSongOfAuthorFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, 0)
                .add(R.id.fullMusicControl, listSongOfAuthorFragment, "danhsach")
                .addToBackStack("danhsach")
                .commit();
    }

    public void dataChange(String data) {
//        adapter = new MusicPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (data == "sortSongAZ" || data == "update" || data == "sortSongZA")
            adapter.updateData(data);

        mViewPager.getAdapter().notifyDataSetChanged();
    }

    public void initView() {
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayoutMusic);
        mTxtAppName = findViewById(R.id.txtAppName);
        frameLayout = findViewById(R.id.fullMusicControl);
    }

    public void initAction() {
        mOnListenerControl = new OnListenerControl() {
            @Override
            public void onClickedBtnPlay() {
                if (!mIsConnected) {
                    return;
                }
                mMusicControl.playMusic();
            }

            @Override
            public void onClickedBtnPause() {
                if (!mIsConnected) {
                    return;
                }
                mMusicControl.pauseMusic();
            }

            @Override
            public void onClickedBtnNextAndPrevious() {
                if (!mIsConnected) return;
                mMusicControl.nextMusic();
            }

            @Override
            public void onClickedSeekBar(int pos) {
                mMusicControl.updateSeekBar(pos);
            }
        };
    }

    public void addViewPagerMusic() {
//        adapter = new MusicPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void addFragmentMusicControl() {
        mMusicControlFragment = new MusicControlFragment();
        mMusicControlFragment.setArguments(bundle);
        mMusicControlFragment.setOnControl(mOnListenerControl);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fullMusicControl, mMusicControlFragment, "musiccontrol")
                .addToBackStack("f")
                .commitAllowingStateLoss();
    }

    public void addFragmentFullMusicControl() {
        mFullMusicControlFragment = new FullMusicControlFragment();
        mFullMusicControlFragment.setArguments(bundle);
        mFullMusicControlFragment.setOnControl(mOnListenerControl);
        mFullMusicControlFragment.setMaxSeekBar(mMusicControl.getDurationSong());
        mFullMusicControlFragment.setProgressSeekBar(mMusicControl.getCurrentPosition());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, 0)
                .add(R.id.fullMusicControl, mFullMusicControlFragment, "fullmusiccontrol")
                .addToBackStack("full")
                .commitAllowingStateLoss();
        mFullMusicControlFragment.updateSeekBar();
    }

    private void connectedService() {
        Intent intent = new Intent(MainActivity.this, MusicControl.class);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicControl.MyBinder binder = (MusicControl.MyBinder) service;
                mMusicControl = binder.getMusicControl();
                if (mMusicControl != null) {
                    mIsConnected = true;
                    mMusicControl.playMusic();
                    if (mMusicControlFragment == null || mFullMusicControlFragment == null) {
                        addFragmentMusicControl();
                    }
                    addFragmentFullMusicControl();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mMusicControl = null;
                mIsConnected = false;
            }
        };
        String song = "Song";
        intent.setAction(song);
        intent.putExtra("path", path);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();//k luu lai gia tri
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (mIsConnected == true) {
            unbindService(mServiceConnection);
            mIsConnected = false;

        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
//        for (Fragment fragment : fragmentList) {
//            if (fragment != null && fragment instanceof OnBackPressFragment) {
//                ((OnBackPressFragment) fragment).onBackPress();
//            }
//        }
        super.onBackPressed();
    }
}
