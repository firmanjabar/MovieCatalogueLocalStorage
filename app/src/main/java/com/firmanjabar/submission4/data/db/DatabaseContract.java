package com.firmanjabar.submission4.data.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {

    public static final String AUTHORITY = "com.firmanjabar.submission4";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class FavoriteColumns implements BaseColumns {

        public static String TABLE_FAVORITE_MOVIE = "favorite_movie";
        public static String TABLE_FAVORITE_TV = "favorite_tv";

        public static String _ID = "id";
        public static String POSTER = "poster";
        public static String TITLE = "title";
        public static String OVERVIEW = "overview";
        public static String DATE = "date";
        public static String VOTE = "vote";
        public static String TYPE = "type";

        public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE_MOVIE)
                .build();

        public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE_TV)
                .build();

    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble( cursor.getColumnIndex(columnName) );
    }
}
