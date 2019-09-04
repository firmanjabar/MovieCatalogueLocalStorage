package com.firmanjabar.submission4.feature.tv;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class TvViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    TvViewModelFactory ( Context context ) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TvViewModel.class)) {
            return (T) new TvViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
