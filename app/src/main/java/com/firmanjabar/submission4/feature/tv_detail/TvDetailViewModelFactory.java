package com.firmanjabar.submission4.feature.tv_detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class TvDetailViewModelFactory implements ViewModelProvider.Factory {

    private Context context;
    private String id;

    TvDetailViewModelFactory ( Context context, String id ) {
        this.context = context;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TvDetailViewModel.class)) {
            return (T) new TvDetailViewModel(context,id);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
