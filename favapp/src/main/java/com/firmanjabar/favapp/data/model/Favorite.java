package com.firmanjabar.favapp.data.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.firmanjabar.favapp.data.db.DatabaseContract.FavoriteColumns;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static com.firmanjabar.favapp.data.db.DatabaseContract.getColumnDouble;
import static com.firmanjabar.favapp.data.db.DatabaseContract.getColumnString;

public class Favorite extends RealmObject implements Parcelable{

    @PrimaryKey
    private String id;

    private String poster_path;
    private String title;
    private String overview;
    private String release_date;
    private double vote_average;

    private String type;

    public Favorite(String id, String poster_path, String title, String overview, String release_date, double vote_average, String type) {
        this.id = id;
        this.poster_path = poster_path;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.type = type;
    }

    public Favorite() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle ( String title ) {
        this.title = title;
    }

    public String getOverview () {
        return overview;
    }

    public void setOverview ( String overview ) {
        this.overview = overview;
    }

    public String getRelease_date () {
        return release_date;
    }

    public void setRelease_date ( String release_date ) {
        this.release_date = release_date;
    }

    public double getVote_average () {
        return vote_average;
    }

    public void setVote_average ( double vote_average ) {
        this.vote_average = vote_average;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Favorite(Cursor cursor) {
        this.id = getColumnString(cursor, FavoriteColumns._ID);
        this.poster_path = getColumnString(cursor, FavoriteColumns.POSTER);
        this.title = getColumnString(cursor, FavoriteColumns.TITLE);
        this.overview = getColumnString(cursor, FavoriteColumns.OVERVIEW);
        this.release_date = getColumnString(cursor, FavoriteColumns.DATE);
        this.vote_average = getColumnDouble(cursor, FavoriteColumns.VOTE);
        this.type = getColumnString(cursor, FavoriteColumns.TYPE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.poster_path);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.type);
    }

    protected Favorite(Parcel in) {
        this.id = in.readString();
        this.poster_path = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.vote_average = in.readDouble();
        this.type = in.readString();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel source) {
            return new Favorite(source);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
}
