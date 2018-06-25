package com.example.android.moviesrus2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.moviesrus2.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.example.android.moviesrus2.MovieContract.MovieEntry.COLUMN_TITLE;
import static com.example.android.moviesrus2.MovieContract.MovieEntry.CONTENT_URI;
import static com.example.android.moviesrus2.MovieContract.MovieEntry.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {
    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_ID = 101;
    public static final int FAVORITE_BY_MOVIE_ID = 102;

    private MovieDbHelper mMovieDbHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITES + "/#", FAVORITE_WITH_ID);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITES + "/" + COLUMN_MOVIE_ID + "/#", FAVORITE_BY_MOVIE_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        int match = mUriMatcher.match(uri);

        Cursor retCursor = null;

        switch (match) {
            case FAVORITES:
                retCursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (retCursor != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);

        switch (match) {
            case FAVORITES:
                return "vnd.android.cursor.dir" + "/" + MovieContract.AUTHORITY + "/" + MovieContract.PATH_FAVORITES;
            case FAVORITE_WITH_ID:
                return "vnd.android.cursor.item" + "/" + MovieContract.AUTHORITY + "/" + MovieContract.PATH_FAVORITES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);

        Uri returnUri;

        switch(match) {
            case FAVORITES:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        Log.v("MATCH", "Match: " + match);
        int favoritesDeleted = 0;

        switch(match) {
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);

                favoritesDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});

                break;
            case FAVORITE_BY_MOVIE_ID:
                String mid = uri.getPathSegments().get(2);
                favoritesDeleted = db.delete(TABLE_NAME, COLUMN_MOVIE_ID + "=?", new String[]{mid});
                break;
            default:
                Log.v("DELETE", "Delete uri: " + uri);
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if(favoritesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favoritesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int favoritesUpdated;
        int match = mUriMatcher.match(uri);

        switch(match) {
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                favoritesUpdated = mMovieDbHelper.getWritableDatabase().update(TABLE_NAME,
                        values,
                        "_id=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return favoritesUpdated;
    }
}
