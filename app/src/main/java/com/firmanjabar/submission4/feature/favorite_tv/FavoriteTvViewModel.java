package com.firmanjabar.submission4.feature.favorite_tv;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.firmanjabar.submission4.data.model.Favorite;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

class FavoriteTvViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Favorite>> response = new MutableLiveData<>();
    private ArrayList<Favorite> tvshow = new ArrayList<>();

    FavoriteTvViewModel ( Context context ) {
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
        tvshow.clear();
        RealmResults<Favorite> favorites = realm.where(Favorite.class).equalTo("type", "tvshow").findAll();
        if (favorites != null && favorites.size()>0){
            tvshow.addAll(favorites);
        }
        setData(tvshow);
    }
}
