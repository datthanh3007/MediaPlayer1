package com.example.mockprojectcampuslink.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.model.Data;
import com.example.mockprojectcampuslink.model.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicControl extends Service {

    private MediaPlayer mMediaPlayer;
    private String mPath;
    private Uri mUri;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals("Song")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mPath = bundle.getString("path");
                mUri = Uri.parse(mPath);
                mMediaPlayer = MediaPlayer.create(getApplication(), mUri);
            }
        }
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public MusicControl getMusicControl() {
            return MusicControl.this;
        }
    }

    public int getDurationSong() {
        return mMediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public void playMusic() {
        mMediaPlayer.start();
    }

    public void stopMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public void pauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void nextMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer.start();
        }
    }

    public void updateSeekBar(int pos) {
        mMediaPlayer.seekTo(pos);
    }
}
