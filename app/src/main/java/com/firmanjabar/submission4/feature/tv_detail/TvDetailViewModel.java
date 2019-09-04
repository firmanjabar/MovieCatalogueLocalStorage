package com.firmanjabar.submission4.feature.tv_detail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.widget.Toast;

import com.firmanjabar.submission4.data.api.RetroServer;
import com.firmanjabar.submission4.data.model.Favorite;
import com.firmanjabar.submission4.data.model.ReviewResponse;
import com.firmanjabar.submission4.data.model.TvDetail;
import com.firmanjabar.submission4.data.model.VideoResponse;
import com.firmanjabar.submission4.utils.Constant;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class TvDetailViewModel extends ViewModel {

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<TvDetail> tvResponse = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    private final MutableLiveData<VideoResponse> videoResponse = new MutableLiveData<>();
    private final MutableLiveData<ReviewResponse> reviewResponse = new MutableLiveData<>();

    @SuppressLint("StaticFieldLeak")
    private Context context;

    TvDetailViewModel ( Context context, String id){
        this.context = context;
        loadTv(id);
        loadVideos(id);
        loadReviews(id);
    }

    private void onErrorVideos(Throwable e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        videoResponse.setValue(null);
    }
    private void setDataVideos(VideoResponse videoResponse) {
        this.videoResponse.setValue(videoResponse);
    }
    private void onErrorReviews(Throwable e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        reviewResponse.setValue(null);
    }
    private void setDataReviews(ReviewResponse reviews) {
        this.reviewResponse.setValue(reviews);
    }

    private void loadVideos ( String id ) {
        disposable.add(
                RetroServer
                        .getRequestService()
                        .getTvVideos(id, Constant.API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setDataVideos, this::onErrorVideos)
        );
    }

    private void loadReviews ( String id ) {
        disposable.add(
                RetroServer
                        .getRequestService()
                        .getTvReviews(id, Constant.API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setDataReviews, this::onErrorReviews)
        );
    }

    @Override
    protected void onCleared() {
        disposable.clear();
    }

    MutableLiveData<TvDetail> getTvResponse() {
        return tvResponse;
    }
    MutableLiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }
    MutableLiveData<VideoResponse> getVideoResponse() {
        return videoResponse;
    }
    MutableLiveData<ReviewResponse> getReviewResponse() {
        return reviewResponse;
    }

    void changeFavState (){
        TvDetail tvDetail = tvResponse.getValue();
        if (tvDetail != null){
            try (Realm realm = Realm.getDefaultInstance()) {
                if (!isFavorite.getValue()) {
                    realm.executeTransaction(realm1 -> {
                        Favorite favorite = realm1.createObject(Favorite.class, tvDetail.getId());
                        favorite.setPoster_path(tvDetail.getPoster_path());
                        favorite.setTitle(tvDetail.getName());
                        favorite.setOverview(tvDetail.getOverview());
                        favorite.setRelease_date(tvDetail.getFirst_air_date());
                        favorite.setVote_average(tvDetail.getVote_average());
                        favorite.setType("tvshow");
                        realm1.insertOrUpdate(favorite);
                    });
                } else {
                    realm.executeTransaction(realm1 -> {
                        Favorite favorite = realm1.where(Favorite.class)
                                .equalTo("id", tvDetail.getId())
                                .equalTo("type", "tvshow")
                                .findFirst();
                        if (favorite != null) {
                            favorite.deleteFromRealm();
                        }
                    });
                }
            }
        }
        isFavorite.setValue(!isFavorite.getValue());
    }

    private void onErrorMovie(Throwable e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        tvResponse.setValue(null);
    }
    private void setDataMovie( TvDetail tvDetail ) {
        tvResponse.setValue(tvDetail);
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<Favorite> results = realm.where(Favorite.class)
                    .equalTo("id", tvDetail.getId())
                    .equalTo("type", "tvshow")
                    .findAll();
            Boolean valid = results.size() > 0;
            isFavorite.setValue(valid);
        }
    }

    private void loadTv ( String id ){
        disposable.add(
                RetroServer
                        .getRequestService()
                        .getTvDetail(id, Constant.API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setDataMovie, this::onErrorMovie)
        );
    }
}
