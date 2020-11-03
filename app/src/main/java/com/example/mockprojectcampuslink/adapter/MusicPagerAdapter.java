package com.example.mockprojectcampuslink.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.fragment.GenreFragment;
import com.example.mockprojectcampuslink.fragment.ListAuthorFragment;
import com.example.mockprojectcampuslink.fragment.ListSongFragment;
import com.example.mockprojectcampuslink.fragment.PlayListFragment;
import com.example.mockprojectcampuslink.listener.updateData;

public class MusicPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private String TAG = "";

    public MusicPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ListSongFragment();
        } else if (position == 1){
            return new PlayListFragment();
        } else if (position == 2){
            return new ListAuthorFragment();
        } else {
            return new GenreFragment();
        }
    }

    public void updateData(String data) {
        if (data=="sortSongAZ" || data=="sortSongZA" || data=="update") TAG = data;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof updateData && TAG == "update") {
            ((ListSongFragment)object).update();
        } else if (object instanceof updateData && TAG == "sortSongAZ") {
            ((ListSongFragment)object).sortSongAZ();
        } else if (object instanceof updateData && TAG == "sortSongZA") {
            ((ListSongFragment)object).sortSongZA();
        }
        return super.getItemPosition(object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.bat_hat);
            case 1:
                return mContext.getString(R.string.playlist);
            case 2:
                return mContext.getString(R.string.ca_si);
            case 3:
                return mContext.getString(R.string.the_loai);
            default:
                return null;
        }
    }
}
