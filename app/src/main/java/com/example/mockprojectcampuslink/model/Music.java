package com.example.mockprojectcampuslink.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Music implements Parcelable {

    String song, author, path, genres;
    Bitmap bitmap;

    public Music(String song, String author, String path, String genres, Bitmap bitmap){
        this.song = song;
        this.author = author;
        this.path = path;
        this.genres = genres;
        this.bitmap = bitmap;
    }

    protected Music(Parcel in) {
        song = in.readString();
        author = in.readString();
        path = in.readString();
        genres = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public String getSong() {
        return song;
    }

    public String getAuthor() {
        return author;
    }

    public String getPath() {
        return path;
    }

    public String getGenres() {
        return genres;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(song);
        parcel.writeString(author);
        parcel.writeString(path);
        parcel.writeString(genres);
        parcel.writeParcelable(bitmap, i);
    }
}
