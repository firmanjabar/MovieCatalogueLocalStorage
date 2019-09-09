package com.firmanjabar.favapp.feature.favorite_tv;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.firmanjabar.favapp.data.db.DatabaseContract;
import com.firmanjabar.favapp.data.model.Favorite;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.firmanjabar.favapp.data.db.DatabaseContract.getColumnDouble;
import static com.firmanjabar.favapp.data.db.DatabaseContract.getColumnString;

class FavoriteTvViewModel extends ViewModel {

    @SuppressLint("StaticFieldLeak")
    public Context context;
    private final MutableLiveData<ArrayList<Favorite>> response = new MutableLiveData<>();

    FavoriteTvViewModel ( Context context ) {
        this.context = context;
        loadFavoriteMovie(context);
    }

    void loadFavoriteMovie(Context context){
        new AsyncFavMovie(context).execute();
    }

    MutableLiveData<ArrayList<Favorite>> getResponse() {
        return response;
    }

    private void setData(ArrayList<Favorite> movieResponse) {
        response.setValue(movieResponse);
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncFavMovie extends AsyncTask<Void, Void, Cursor> {
        private WeakReference<Context> weakContext;
        ArrayList<Favorite> favorites;

        AsyncFavMovie(Context context) {
            this.weakContext = new WeakReference<>(context);
            favorites = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(DatabaseContract.FavoriteColumns.CONTENT_URI_TV, null,null,null,null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            favorites.clear();
            if (cursor != null){
                try {
                    if (cursor.moveToFirst()){
                        do {
                            Favorite fav = new Favorite(
                                    getColumnString(cursor, DatabaseContract.FavoriteColumns._ID),
                                    getColumnString(cursor, DatabaseContract.FavoriteColumns.POSTER),
                                    getColumnString(cursor, DatabaseContract.FavoriteColumns.TITLE),
                                    getColumnString(cursor, DatabaseContract.FavoriteColumns.OVERVIEW),
                                    getColumnString(cursor, DatabaseContract.FavoriteColumns.DATE),
                                    getColumnDouble(cursor, DatabaseContract.FavoriteColumns.VOTE),
                                    getColumnString(cursor, DatabaseContract.FavoriteColumns.TYPE)
                            );
                            favorites.add(fav);
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e){
                    setData(favorites);
                    cursor.close();
                } finally {
                    setData(favorites);
                    cursor.close();
                }
            }
        }
    }
}