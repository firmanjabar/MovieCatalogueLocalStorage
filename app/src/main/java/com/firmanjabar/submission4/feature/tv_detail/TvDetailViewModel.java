package com.firmanjabar.submission4.feature.tv_detail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.firmanjabar.submission4.data.api.RetroServer;
import com.firmanjabar.submission4.data.model.ReviewResponse;
import com.firmanjabar.submission4.data.model.TvDetail;
import com.firmanjabar.submission4.data.model.VideoResponse;
import com.firmanjabar.submission4.utils.Constant;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.firmanjabar.submission4.data.db.DatabaseContract.FavoriteColumns.*;

public class TvDetailViewModel extends ViewModel {

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<TvDetail> tvResponse = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    private final MutableLiveData<VideoResponse> videoResponse = new MutableLiveData<>();
    private final MutableLiveData<ReviewResponse> reviewResponse = new MutableLiveData<>();
    private Uri mUri;
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

    private void loadFavoriteTVShowWithId ( Context context, String id ){
        new AsyncFavTVShowWithID(context, id).execute();
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
        if (tvDetail != null && isFavorite.getValue() != null){
            if (!isFavorite.getValue()) {
                ContentValues values = new ContentValues();
                values.put(_ID, tvDetail.getId());
                values.put(POSTER, tvDetail.getPoster_path());
                values.put(TITLE, tvDetail.getName());
                values.put(OVERVIEW, tvDetail.getOverview());
                values.put(DATE, tvDetail.getFirst_air_date());
                values.put(VOTE, tvDetail.getVote_average());
                values.put(TYPE, "tv");
                context.getContentResolver().insert(CONTENT_URI_TV, values);
            } else {
                mUri = CONTENT_URI_TV
                        .buildUpon()
                        .appendPath(String.valueOf(tvDetail.getId()))
                        .build();
                context.getContentResolver().delete(mUri, null, null);
            }
            isFavorite.setValue(!isFavorite.getValue());
        }
    }


    private void onErrorMovie(Throwable e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        tvResponse.setValue(null);
    }
    private void setDataMovie( TvDetail tvDetail ) {
        tvResponse.setValue(tvDetail);
        loadFavoriteTVShowWithId(context, tvDetail.getId());
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

    @SuppressLint("StaticFieldLeak")
    public class AsyncFavTVShowWithID extends AsyncTask<Void, Void, Cursor> {
        private WeakReference<Context> weakContext;
        String id;

        AsyncFavTVShowWithID(Context context, String id) {
            this.weakContext = new WeakReference<>(context);
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            mUri = CONTENT_URI_TV
                    .buildUpon()
                    .appendPath(String.valueOf(id))
                    .build();
            return context.getContentResolver().query(mUri, null,null,null,null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            isFavorite.setValue(cursor.getCount()>0);
        }
    }
}
