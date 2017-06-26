package bemonkey.net.udaflux.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import bemonkey.net.udaflux.R;
import bemonkey.net.udaflux.adapters.MoviesAdapter;
import bemonkey.net.udaflux.api.MoviesApi;
import bemonkey.net.udaflux.models.movies.MovieModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.facebook.stetho.Stetho;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GridLayoutManager mGridLayoutManager;
    public MoviesAdapter mAdapter;
    private Unbinder mUnbinder;
    private MovieModel movieRes;
    private String mTitle;

    @BindView(R.id.rv_posters_grid) RecyclerView mRecyclerView;
    @BindView (R.id.pb_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
        if(savedInstanceState != null){
            if (savedInstanceState.containsKey("MOVIES")){
                movieRes = savedInstanceState.getParcelable("MOVIES");
                mTitle = savedInstanceState.getString("TITLE");
                setTitle(mTitle);
            }
        }


        mUnbinder = ButterKnife.bind(this);

        //Creating a new Grid layout manager
        //check orientation and set column number
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mGridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        }else{
            mGridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
        }

        //Setting recyclerView's layout manager
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        //setting the adapter setHasFixedSize option to true
        mRecyclerView.setHasFixedSize(true);
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey("MOVIES")) {
                mAdapter = new MoviesAdapter(getApplicationContext(), movieRes.getResults());
                mRecyclerView.setAdapter(mAdapter);

            }
        }else{
            getPopularData();
        }

    }

    /**
     * Called when activity is create , getting popular movies from API.
     */
    private void getPopularData(){
        mProgressBar.setVisibility(View.VISIBLE);
        Call<MovieModel> call = MoviesApi.get().getPopular("fr-FR", 1);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                movieRes = response.body();
                //Create a new MoviesAdapter
                mAdapter = new MoviesAdapter(getApplicationContext(), movieRes.getResults());
                //creating a new adapter with the context and the data then setting my recyclerView's with this new adapter
                mRecyclerView.setAdapter(mAdapter);
                mTitle = getString(R.string.poular_title);
                setTitle(mTitle);
                showGrid();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                showError();
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("MOVIES", movieRes);
        outState.putString("TITLE", mTitle);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_most_popular:
                getPopularData();
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
}
