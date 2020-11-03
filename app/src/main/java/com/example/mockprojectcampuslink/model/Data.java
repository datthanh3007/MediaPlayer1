package com.example.mockprojectcampuslink.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Data {

    ArrayList<Integer> soLuongBaiHat;
    private static List<Music> mListmusic;
    private List<Music> mListGenre, mListAuthor;
    private static Uri mUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static Cursor mMediaCursor, mGenresCursor;

    private static String[] mProjection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATA
    };

    private static String[] mGenresProjection = {
            MediaStore.Audio.Genres.NAME,
            MediaStore.Audio.Genres._ID
    };

    public Data(Context context) {
        getMusicFromStorage(context);
        listDataGenre();
        listDataAuthor();
        countSong();
    }

    public static void getMusicFromStorage(Context context) {
        mListmusic = new ArrayList<>();

        mMediaCursor = context.getContentResolver().query(mUri, mProjection, MediaStore.Audio.Media.DATA + " like ? ",
                new String[]{"%Zing MP3%"}, null);

        if (mMediaCursor != null) {
            while (mMediaCursor.moveToNext()) {
                String song = mMediaCursor.getString(1);
                String artist = mMediaCursor.getString(2);
                long albumId = mMediaCursor.getLong(3);
                String mPath = mMediaCursor.getString(4);

                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri uri1 = ContentUris.withAppendedId(sArtworkUri, albumId);
                ContentResolver res = context.getContentResolver();
                InputStream in = null;
                try {
                    in = res.openInputStream(uri1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap artwork = BitmapFactory.decodeStream(in);

                int musicID = Integer.parseInt(mMediaCursor.getString(0));
                Uri uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", musicID);
                mGenresCursor = context.getContentResolver().query(uri, mGenresProjection, null, null, null);
                if (mGenresCursor != null) {
                    while (mGenresCursor.moveToNext()) {
                        String genres = mGenresCursor.getString(0);
                        int count = mGenresCursor.getCount();

                        Music music = new Music(song, artist, mPath, genres, artwork);
                        music.setSong(song);
                        music.setAuthor(artist);
                        music.setPath(mPath);
                        music.setGenres(genres);
                        music.setBitmap(artwork);

                        mListmusic.add(music);
                    }

                    mGenresCursor.close();
                }
            }

            mMediaCursor.close();
        }
    }

    public void listDataGenre() {
        mListGenre = new ArrayList<>();
        for (Music music : mListmusic) {
            boolean check = false;
            for (Music music1 : mListGenre) {
                if (music1.getGenres().equals(music.getGenres())) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                mListGenre.add(music);
            }
        }
    }

    public void listDataAuthor() {
        mListAuthor = new ArrayList<>();
        for (Music music : mListmusic) {
            boolean check = false;
            for (Music music1 : mListAuthor) {
                if (music1.getAuthor().equals(music.getAuthor())) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                mListAuthor.add(music);
            }
        }
    }

    public void countSong() {
        soLuongBaiHat = new ArrayList<>();
        for (int i = 0; i < mListmusic.size(); i++) {
            soLuongBaiHat.add(i, 0);
        }
        for (int i = 0; i < mListmusic.size(); i++) {
            for (int j = 0; j < mListAuthor.size(); j++) {
                int dem = 0;
                if (mListmusic.get(i).getAuthor().equals(mListAuthor.get(j).getAuthor())) {
                    dem = soLuongBaiHat.get(j);
                    dem++;
                    soLuongBaiHat.set(j, dem);
                    break;
                }
            }
        }
    }

    public ArrayList<Integer> getCountSong() {
        return soLuongBaiHat;
    }

    public List<Music> getmListGenre() {
        return mListGenre;
    }

    public List<Music> getmListAuthor() {
        return mListAuthor;
    }

    public List<Music> getMusic() {
        return mListmusic;
    }
}
