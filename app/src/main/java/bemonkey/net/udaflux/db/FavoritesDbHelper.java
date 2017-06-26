package bemonkey.net.udaflux.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bmnk on 3/17/17.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "favorites.db";

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoritesContract.Favorites.TABLE_NAME + " (" +
                FavoritesContract.Favorites._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoritesContract.Favorites.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                FavoritesContract.Favorites.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoritesContract.Favorites.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoritesContract.Favorites.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                FavoritesContract.Favorites.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                FavoritesContract.Favorites.COLUMN_POSTER_PATH + " BLOB NOT NULL, " +
                FavoritesContract.Favorites.COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    // In a production app, this method might be modified to ALTER the table
    // instead of dropping it, so that existing data is not deleted.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.Favorites.TABLE_NAME + ";");
        onCreate(db);
    }
}
