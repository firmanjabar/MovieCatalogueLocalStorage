package com.firmanjabar.submission4.feature.favorite_tv;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class FavoriteTvViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    FavoriteTvViewModelFactory ( Context context ) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create( @NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavoriteTvViewModel.class)) {
            return (T) new FavoriteTvViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
