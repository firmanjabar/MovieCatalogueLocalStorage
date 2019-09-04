package com.firmanjabar.submission4.feature.tv;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.widget.Toast;

import com.firmanjabar.submission4.data.api.RetroServer;
import com.firmanjabar.submission4.data.model.TvResponse;
import com.firmanjabar.submission4.utils.Constant;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TvViewModel extends ViewModel {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<TvResponse> response = new MutableLiveData<>();

    TvViewModel ( Context context){
        this.context = context;
        loadMovie();
    }

    @Override
    protected void onCleared() {
        disposable.clear();
    }

    MutableLiveData<TvResponse> getResponse() {
        return response;
    }

    private void onError(Throwable e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        response.setValue(new TvResponse());
    }

    private void setData( TvResponse tvResponse ) {
        response.setValue(tvResponse);
    }

    private void loadMovie (){
        disposable.add(
                RetroServer
                        .getRequestService()
                        .getTv(Constant.API_KEY, Constant.LANGUAGE)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setData, this::onError)
        );
    }
}
