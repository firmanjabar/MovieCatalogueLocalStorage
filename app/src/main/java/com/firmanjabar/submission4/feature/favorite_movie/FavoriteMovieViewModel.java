package com.firmanjabar.submission4.feature.favorite_movie;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.firmanjabar.submission4.data.model.Favorite;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

class FavoriteMovieViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Favorite>> response = new MutableLiveData<>();
    private ArrayList<Favorite> movies = new ArrayList<>();

    FavoriteMovieViewModel(Context context) {
        loadFavoriteMovie();
    }

    MutableLiveData<ArrayList<Favorite>> getResponse() {
        return response;
    }

    private void setData(ArrayList<Favorite> movieResponse) {
        response.setValue(movieResponse);
    }

    void loadFavoriteMovie() {
        Realm realm = Realm.getDefaultInstance();
        try {
            movies.clear();
            RealmResults<Favorite> favorites = realm.where(Favorite.class).equalTo("type", "movie").findAll();
            if (favorites != null && favorites.size()>0){
                movies.addAll(favorites);
            }
            setData(movies);
        } finally {
        }
    }
}
