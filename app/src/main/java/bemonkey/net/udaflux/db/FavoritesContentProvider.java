package bemonkey.net.udaflux.db;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by bmnk on 3/23/17.
 */

public class FavoritesContentProvider extends ContentProvider {
    private static final int  FAVORITES = 100;
    private static final int  FAVORITES_WITH_ID = 101;
    private static UriMatcher  sUriMatcher = buildUriMatcher();
    private FavoritesDbHelper mFavoritesDbHelper;

    public static UriMatcher buildUriMatcher(){
        UriMatcher urimatcher = new UriMatcher(UriMatcher.NO_MATCH);

        urimatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES, FAVORITES);
        urimatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES + "/#", FAVORITES_WITH_ID);

        return urimatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoritesDbHelper = new FavoritesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoritesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor returnedCursor = null;
        switch (match){
            case FAVORITES:
                returnedCursor = db.query(
                        FavoritesContract.Favorites.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (returnedCursor != null) {
            returnedCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return returnedCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case FAVORITES:
                long id = db.insert(FavoritesContract.Favorites.TABLE_NAME,null,values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(FavoritesContract.Favorites.CONTENT_URI, id);
                }else {
                    throw new android.database.SQLException("Failed To insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int favoriteDeleted;
        switch (match){
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                favoriteDeleted = db.delete(FavoritesContract.Favorites.TABLE_NAME, FavoritesContract.Favorites._ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (favoriteDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return favoriteDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
