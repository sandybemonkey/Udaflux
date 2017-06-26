package bemonkey.net.udaflux.ui;

import android.content.Intent;
import android.content.res.Configuration;
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
import bemonkey.net.udaflux.adapters.MoviesAdapter;
import bemonkey.net.udaflux.api.MoviesApi;
import bemonkey.net.udaflux.models.movies.MovieModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopRatedMoviesActivity extends AppCompatActivity {
    private GridLayoutManager mGridLayoutManager;
    private MoviesAdapter mAdapter;
    private MovieModel movieRes;
    public String mTitle;
    private Unbinder mUnbinder;

    @BindView(R.id.rv_posters_grid)
    RecyclerView mRecyclerView;
    @BindView (R.id.pb_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            if (savedInstanceState.containsKey("MOVIES")){
                movieRes = savedInstanceState.getParcelable("MOVIES");
                mTitle = savedInstanceState.getString("TITLE");
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
                setTitle(mTitle);
                mRecyclerView.setAdapter(mAdapter);
            }
        }else{
            getTopRatedData();
        }

    }

    private void getTopRatedData(){
        mProgressBar.setVisibility(View.VISIBLE);
        Call<MovieModel> call = MoviesApi.get().getTopRated("fr-FR", 1);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                movieRes = response.body();
                //Create a new MoviesAdapter
                mAdapter = new MoviesAdapter(getApplicationContext(), movieRes.getResults());
                //creating a new adapter with the context and the data then setting my recyclerView's with this new adapter
                mRecyclerView.setAdapter(mAdapter);
                mTitle = getString(R.string.top_rated_title);
                setTitle(mTitle);
                showGrid();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                showError();
            }
        });
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("MOVIES", movieRes);
        outState.putString("TITLE", mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_rated_menu, menu);
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
        mUnbinder.unbind();
    }
}
