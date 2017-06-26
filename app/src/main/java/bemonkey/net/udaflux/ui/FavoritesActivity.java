package bemonkey.net.udaflux.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import bemonkey.net.udaflux.R;
import bemonkey.net.udaflux.adapters.FavoritesAdapter;
import bemonkey.net.udaflux.db.FavoritesContract;
import bemonkey.net.udaflux.db.FavoritesDbHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoritesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{
    public SQLiteDatabase mDb;
    public FavoritesAdapter mFavoritesAdapter;
    private Unbinder mUnbind;

    private static final int ID_FAVORITES_LOADER = 97;

    @BindView(R.id.rv_posters_grid) RecyclerView mRecyclerView;
    @BindView (R.id.pb_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbind = ButterKnife.bind(this);

        //Creating a new Grid layout manager
        //check orientation and set column number
        GridLayoutManager mGridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mGridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        }else{
            mGridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
        }

        //Setting recyclerView's layout manager
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        //setting the adapter setHasFixedSize option to true
        mRecyclerView.setHasFixedSize(true);
        FavoritesDbHelper favoritesDbHelper = new FavoritesDbHelper(this);
        mDb = favoritesDbHelper.getWritableDatabase();
        getSupportLoaderManager().initLoader(ID_FAVORITES_LOADER, null, this);
    }

    /**
     * This method display the data.
     */
    void showGrid(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * This method display error message.
     */
    void showError(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(ID_FAVORITES_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_FAVORITES_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri moviesQueryUri = FavoritesContract.Favorites.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = FavoritesContract.Favorites.COLUMN_TIMESTAMP+ " ASC";
                return new CursorLoader(this,
                        moviesQueryUri,
                        null,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String mTitle = getString(R.string.favorites_app_bar_title);
        setTitle(mTitle);
        mFavoritesAdapter = new FavoritesAdapter(getApplicationContext(), data);
        mRecyclerView.setAdapter(mFavoritesAdapter);
        showGrid();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoritesAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorites_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_most_popular:
                //getTopRatedData();
                Intent popularIntent = new Intent(this, MainActivity.class);
                startActivity(popularIntent);
                return true;

            case R.id.action_top_rated:
                //getTopRatedData();
                Intent topRatedIntent = new Intent(this, TopRatedMoviesActivity.class);
                startActivity(topRatedIntent);
                return true;

            case R.id.action_favorites:
                Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoritesIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }
}
