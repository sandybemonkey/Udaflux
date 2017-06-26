package bemonkey.net.udaflux.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bmnk on 3/15/17.
 */

public class FavoritesContract {
    public static final String AUTHORITY  = "bemonkey.net.udaflux";

    public static final Uri BASE_URI  = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES  = "favorites";

    public static final class Favorites implements BaseColumns{

        public static final Uri CONTENT_URI  =
                BASE_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_TIMESTAMP = "date";
    }
}
