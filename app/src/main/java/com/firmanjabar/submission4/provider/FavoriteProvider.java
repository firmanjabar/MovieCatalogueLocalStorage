package com.firmanjabar.submission4.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firmanjabar.submission4.data.db.DatabaseContract.FavoriteColumns;
import com.firmanjabar.submission4.data.model.Favorite;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.firmanjabar.submission4.data.db.DatabaseContract.AUTHORITY;
import static com.firmanjabar.submission4.data.db.DatabaseContract.FavoriteColumns.TABLE_FAVORITE_MOVIE;
import static com.firmanjabar.submission4.data.db.DatabaseContract.FavoriteColumns.TABLE_FAVORITE_TV;

public class FavoriteProvider extends ContentProvider {

    private static final int FAVORITE_MOVIE = 1;
    private static final int FAVORITE_MOVIE_ID = 2;
    private static final int FAVORITE_TV= 3;
    private static final int FAVORITE_TV_ID = 4;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_MOVIE, FAVORITE_MOVIE);
        sUriMatcher.addURI(AUTHORITY,
                TABLE_FAVORITE_MOVIE+ "/#",
                FAVORITE_MOVIE_ID);
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_TV, FAVORITE_TV);
        sUriMatcher.addURI(AUTHORITY,
                TABLE_FAVORITE_TV+ "/#",
                FAVORITE_TV_ID);
    }


    @Override
    public boolean onCreate() {
        Realm.init(Objects.requireNonNull(getContext()));
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        //Get Realm Instance
        MatrixCursor myCursor = new MatrixCursor( new String[]{FavoriteColumns._ID
                , FavoriteColumns.POSTER
                , FavoriteColumns.TITLE
                , FavoriteColumns.OVERVIEW
                , FavoriteColumns.DATE
                , FavoriteColumns.VOTE
                , FavoriteColumns.TYPE
        });

        try (Realm realm = Realm.getDefaultInstance()) {
            switch (sUriMatcher.match(uri)) {
                case FAVORITE_MOVIE:
                    RealmResults<Favorite> favoriteRealmResults = realm.where(Favorite.class).equalTo("type", "movie").findAll();
                    for (Favorite favMovie : favoriteRealmResults) {
                        Object[] rowData = new Object[]{favMovie.getId(), favMovie.getPoster_path(), favMovie.getTitle(), favMovie.getOverview(), favMovie.getRelease_date(), favMovie.getVote_average(), favMovie.getType()};
                        myCursor.addRow(rowData);
                        Log.v("RealmDB", favMovie.toString());
                    }
                    break;
                case FAVORITE_MOVIE_ID:
                    String id = uri.getPathSegments().get(1);
                    Favorite favMovie = realm.where(Favorite.class).equalTo("id", id).equalTo("type", "movie").findFirst();
                    if (favMovie != null) {
                        myCursor.addRow(new Object[]{favMovie.getId(), favMovie.getPoster_path(), favMovie.getTitle(), favMovie.getOverview(), favMovie.getRelease_date(), favMovie.getVote_average(), favMovie.getType()});
                        Log.v("RealmDB", favMovie.toString());
                    }
                    break;
                case FAVORITE_TV:
                    favoriteRealmResults = realm.where(Favorite.class).equalTo("type", "tv").findAll();
                    for (Favorite favTV : favoriteRealmResults) {
                        Object[] rowData = new Object[]{favTV.getId(), favTV.getPoster_path(), favTV.getTitle(), favTV.getOverview(), favTV.getRelease_date(), favTV.getVote_average(), favTV.getType()};
                        myCursor.addRow(rowData);
                        Log.v("RealmDB", favTV.toString());
                    }
                    break;
                case FAVORITE_TV_ID:
                    String id_tv = uri.getPathSegments().get(1);
                    Favorite favTV = realm.where(Favorite.class).equalTo("id", id_tv).equalTo("type", "tv").findFirst();
                    if (favTV != null) {
                        myCursor.addRow(new Object[]{favTV.getId(), favTV.getPoster_path(), favTV.getTitle(), favTV.getOverview(), favTV.getRelease_date(), favTV.getVote_average(), favTV.getType()});
                        Log.v("RealmDB", favTV.toString());
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            myCursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);

        }
        return myCursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        Uri returnUri;
        try (Realm realm = Realm.getDefaultInstance()) {
            switch (sUriMatcher.match(uri)) {
                case FAVORITE_MOVIE:
                case FAVORITE_TV:
                    realm.executeTransaction(realm1 -> {
                        Favorite fav = realm1.createObject(Favorite.class, contentValues.get(FavoriteColumns._ID).toString());
                        fav.setPoster_path(contentValues.get(FavoriteColumns.POSTER).toString());
                        fav.setTitle(contentValues.get(FavoriteColumns.TITLE).toString());
                        fav.setOverview(contentValues.get(FavoriteColumns.OVERVIEW).toString());
                        fav.setRelease_date(contentValues.get(FavoriteColumns.DATE).toString());
                        fav.setVote_average((Double) contentValues.get(FavoriteColumns.VOTE));
                        fav.setType(contentValues.get(FavoriteColumns.TYPE).toString());
                        realm1.insertOrUpdate(fav);
                    });
                    returnUri = ContentUris.withAppendedId(
                            (sUriMatcher.match(uri) == FAVORITE_MOVIE_ID) ? FavoriteColumns.CONTENT_URI_MOVIE : FavoriteColumns.CONTENT_URI_TV,
                            Long.parseLong(contentValues.get(FavoriteColumns._ID).toString()));
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updated = 0;

        try (Realm realm = Realm.getDefaultInstance()) {
            String id;
            Favorite fav;
            String type = (sUriMatcher.match(uri) == FAVORITE_MOVIE_ID) ? "movie" : "tv";
            id = uri.getPathSegments().get(1);
            fav = realm.where(Favorite.class).equalTo("id", id).equalTo("type", type).findFirst();
            realm.beginTransaction();
            if (fav != null) {
                fav.setPoster_path(contentValues.get(FavoriteColumns.POSTER).toString());
                fav.setTitle(contentValues.get(FavoriteColumns.TITLE).toString());
                fav.setOverview(contentValues.get(FavoriteColumns.OVERVIEW).toString());
                fav.setRelease_date(contentValues.get(FavoriteColumns.DATE).toString());
                fav.setVote_average((Double) contentValues.get(FavoriteColumns.VOTE));
                fav.setType(contentValues.get(FavoriteColumns.TYPE).toString());
                updated++;
            }
            realm.commitTransaction();
        }

        if (updated > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] strings) {
        int count = 0;
        try (Realm realm = Realm.getDefaultInstance()) {
            String type;
            switch (sUriMatcher.match(uri)) {
                case FAVORITE_MOVIE:
                case FAVORITE_TV:
                    type = (sUriMatcher.match(uri) == FAVORITE_MOVIE) ? "movie" : "tv";
                    selection = (selection == null) ? "1" : selection;
                    RealmResults<Favorite> favoriteRealmResults = realm.where(Favorite.class).equalTo(selection, strings[0]).equalTo("type", type).findAll();
                    realm.beginTransaction();
                    favoriteRealmResults.deleteAllFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                case FAVORITE_MOVIE_ID:
                case FAVORITE_TV_ID:
                    type = (sUriMatcher.match(uri) == FAVORITE_MOVIE_ID) ? "movie" : "tv";
                    String id = String.valueOf(ContentUris.parseId(uri));
                    Favorite fav = realm.where(Favorite.class).equalTo("id", id).equalTo("type", type).findFirst();
                    realm.beginTransaction();
                    if (fav != null) {
                        fav.deleteFromRealm();
                        count++;
                    }
                    realm.commitTransaction();
                    break;
                default:
                    throw new IllegalArgumentException("Illegal delete URI");
            }

        }

        if (count > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return count;
    }
}
