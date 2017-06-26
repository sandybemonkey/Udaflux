package bemonkey.net.udaflux.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import bemonkey.net.udaflux.R;
import bemonkey.net.udaflux.adapters.TrailerAdapter;
import bemonkey.net.udaflux.models.trailers.TrailerModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TrailersActivity extends AppCompatActivity {
    private int mMovieId;
    private TrailerModel mTrailerResult;
    @BindView(R.id.rv_trailers_list) RecyclerView mRecyclerView;
    private Unbinder mUnbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        mMovieId = getIntent().getExtras().getInt("MOVIE_ID");

        mUnbind = ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        TrailerAdapter mTrailerAdapter;
        if(savedInstanceState != null){
            if (savedInstanceState.containsKey("TRAILERS")){
                mTrailerResult = savedInstanceState.getParcelable("TRAILERS");
                mMovieId = savedInstanceState.getInt("MOVIE_ID");
                mTrailerAdapter = new TrailerAdapter(
                        getApplicationContext(),
                        mTrailerResult.getResults()
                );
                mRecyclerView.setAdapter(mTrailerAdapter);
            }
        }else{
            mTrailerResult = getIntent().getExtras().getParcelable("TRAILERS");
            mTrailerAdapter = new TrailerAdapter(
                    getApplicationContext(),
                    mTrailerResult.getResults()
            );
            mRecyclerView.setAdapter(mTrailerAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("TRAILERS", mTrailerResult);
        outState.putInt("MOVIE_ID", mMovieId);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }
}


