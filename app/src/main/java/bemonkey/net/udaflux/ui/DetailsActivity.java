package bemonkey.net.udaflux.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bemonkey.net.udaflux.R;
import bemonkey.net.udaflux.api.MoviesApi;
import bemonkey.net.udaflux.db.FavoritesContract;
import bemonkey.net.udaflux.db.FavoritesDbHelper;
import bemonkey.net.udaflux.models.movies.MovieResult;
import bemonkey.net.udaflux.models.reviews.ReviewModel;
import bemonkey.net.udaflux.models.trailers.TrailerModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static bemonkey.net.udaflux.R.string.poster_prefix_url;

public class DetailsActivity extends AppCompatActivity{

    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.tv_plot_synopsis) TextView mSynopsis;
    @BindView(R.id.tv_user_rating) TextView mUserRating;
    @BindView(R.id.tv_number_of_reviews) TextView mNumberOfReviews;
    //@BindView(R.id.iv_poster) ImageView mPoster;
    @BindView(R.id.floatingActionButton) FloatingActionButton mFloatingButton;
    @BindView(R.id.bt_trailer) Button mTrailersButton;

    private Unbinder mUnbinder;
    private MovieResult movieParcel;
    private TrailerModel mTrailerResult;
    private ReviewModel mReviewResults;
    private static final String  KEY = "movies";
    private SQLiteDatabase mDb;
    private Target mTarget;

    private long mFavId;
    private int mFavMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        mUnbinder = ButterKnife.bind(this);

        if(savedInstanceState != null){
            if (savedInstanceState.containsKey(KEY)){
                movieParcel = savedInstanceState.getParcelable(KEY);
            }
        }else{
            //Getting my Parcel
            movieParcel = getIntent().getExtras().getParcelable("movies");
        }

        FavoritesDbHelper favoritesDbHelper = new FavoritesDbHelper(this);

        mDb = favoritesDbHelper.getWritableDatabase();

        mTarget = new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mTitle.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        checkIfIsFavorite();
        initView(movieParcel);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("Bemonkey", "onSaveInstance");
        outState.putParcelable("movies", movieParcel);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    /**
     * Called when activity is create , calling other function to sets Up the views.
     *
     * @param movie data from request .
     */
    public void initView(MovieResult movie){
        setUpViews(movie);
        getReviews();
        getTrailers();
    }

    /**
     * Called in initView, sets Up the views.
     *
     * @param movie data from request .
     */
    void setUpViews(MovieResult movie){
        //Getting some string value to make the right call for the poster and
        // to and information to the ui
        String prefix = getString(poster_prefix_url);
        String user_rating_sufix = getBaseContext().getString(R.string.user_rating_sufix);

        //Setting each views with his informations
        setTitle(movie.getTitle());
        mReleaseDate.setText(movie.getReleaseDate().substring(0,4));
        mSynopsis.setText(movie.getOverview());
        mUserRating.setText(movie.getVoteAverage() + user_rating_sufix);

        //Getting and setting the poster
        ImageView img = new ImageView(this);
        Picasso.with(this)
                .load( prefix + movie.getPosterPath())
                .into(mTarget);

    }

    /**
     * Called in initView, sets Up the views.
     * Making a call to API and get reviews for one movie
     */
    public void getReviews(){
        Call<ReviewModel> call = MoviesApi.get()
                .getMovieReviews(String.valueOf(movieParcel.getId()));

        call.enqueue(new Callback<ReviewModel>() {
            @Override
            public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
                mReviewResults = response.body();
                int reviewsNumber = response.body().getResults().size();

                switch (reviewsNumber){
                    case 0:
                        mNumberOfReviews.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        mNumberOfReviews.setText(String.format("%d review", reviewsNumber));
                        break;
                    default:
                        mNumberOfReviews.setText(String.format("%d reviews", reviewsNumber));
                }

            }
            @Override
            public void onFailure(Call<ReviewModel> call, Throwable t) {
                throw new UnsupportedOperationException("Not implemented");
            }
        });
    }

    /**
     * Called in initView, sets Up the views.
     * Making a call to API and get reviews for one movie
     */
    public void getTrailers(){
        Call<TrailerModel> call = MoviesApi.get()
                .getMovieTrailers(String.valueOf(String.valueOf(movieParcel.getId())));

        call.enqueue(new Callback<TrailerModel>() {
            @Override
            public void onResponse(Call<TrailerModel> call, Response<TrailerModel> response) {
                mTrailerResult = response.body();
                int trailersNumber = response.body().getResults().size();
                switch (trailersNumber){
                    case 0:
                        mTrailersButton.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        mTrailersButton.setText(String.format("%d trailer", trailersNumber));
                        break;
                    default:
                        mTrailersButton.setText(String.format("%d trailers", trailersNumber));
                }
            }

            @Override
            public void onFailure(Call<TrailerModel> call, Throwable t) {
                throw new UnsupportedOperationException("Not implemented");
            }
        });
    }


    /**
     * Called when clinking on the floating Button.
     *
     * @param view The View that is clicked.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void floatingButtonActions(View view){
        try (Cursor cursor = isFavorite()) {
            while (cursor.moveToNext()) {
                mFavId = cursor.getLong(0);
                mFavMovieId = cursor.getInt(1);
            }

            if(cursor.getCount() != 0){
                if(mFavMovieId == movieParcel.getId()){
                    removeFavorites(mFavId);
                    mFloatingButton.setImageResource(R.drawable.ic_stars_black_24dp);
                }
            }else{
                addToFavorites();
                mFloatingButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            }
        }

    }

    /**
     * Called when clinking on the floating Button.
     */
    public void addToFavorites() {

        ContentValues newFavorite = new ContentValues();
        newFavorite.put(FavoritesContract.Favorites.COLUMN_MOVIE_ID, movieParcel.getId());
        newFavorite.put(FavoritesContract.Favorites.COLUMN_TITLE, movieParcel.getTitle());
        newFavorite.put(FavoritesContract.Favorites.COLUMN_RELEASE_DATE, movieParcel.getReleaseDate());
        newFavorite.put(FavoritesContract.Favorites.COLUMN_VOTE_AVERAGE, movieParcel.getVoteAverage());
        newFavorite.put(FavoritesContract.Favorites.COLUMN_POSTER_PATH, movieParcel.getPosterPath());
        newFavorite.put(FavoritesContract.Favorites.COLUMN_SYNOPSIS, movieParcel.getOverview());

        Uri uri = getContentResolver().insert(FavoritesContract.Favorites.CONTENT_URI, newFavorite);
        Context context = getApplicationContext();
        if(uri != null){
            CharSequence text = movieParcel.getTitle() + " Added to Favorites";
            showMessage(context, text);
        }else{
            showError(context);
        }
    }

    /**
     * Called when clinking on the floating Button.
     *
     * @param vFavId Type long represente the movies Id.
     */
    public void removeFavorites(long vFavId){
        Uri baseUri = FavoritesContract.Favorites.CONTENT_URI;
        Uri queryUri = baseUri.buildUpon().appendPath(String.valueOf(vFavId)).build();

        int favoriteDeleted = getContentResolver().delete(queryUri, null, null);
        if (favoriteDeleted != 0){
            Context context = getApplicationContext();
            CharSequence text = movieParcel.getTitle() + " Removed from Favorites";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Called when clinking on link and start relative activity.
     *
     * @param view The View that is clicked.
     */
    public void seeReviews(View view) {
        Intent intent = new Intent(getApplicationContext(), ReviewsActivity.class);
        intent.putExtra("REVIEWS", mReviewResults);
        startActivity(intent);
    }

    /**
     * Called when clinking on link and start relative activity.
     *
     * @param view The View that is clicked.
     */
    public void seeTrailers(View view) {
        Intent intent = new Intent(getApplicationContext(), TrailersActivity.class);
        intent.putExtra("TRAILERS", mTrailerResult);
        startActivity(intent);
    }

    /**
     * Helper showing a succes message when adding or deleting favorite.
     */
    private void showMessage(Context context, CharSequence text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Helper showing a error message when adding or deleting favorite.
     */
    private void showError(Context context){
        Toast toast = Toast.makeText(context, "Something goes wrong!", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Helper that check if movie is  favorite.
     */
    private void checkIfIsFavorite(){
        Cursor cursor = isFavorite();
        int count = cursor.getCount();
        if (count > 0){
            mFloatingButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            mFloatingButton.setImageResource(R.drawable.ic_stars_black_24dp);
            cursor.close();
        }
    }

    /**
     * Helper taht query favorite db to get favorite row if exist.
     */
    private Cursor isFavorite(){
        String q="SELECT * FROM favorites WHERE movie_id='" + movieParcel.getId() +"'";

        Cursor cursor = null;
        cursor = mDb.rawQuery(q, null);
        if (cursor != null){
            return cursor;
        }else{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }

}
