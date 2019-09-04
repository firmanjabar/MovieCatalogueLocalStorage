package com.firmanjabar.submission4.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Favorite extends RealmObject {

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
}
