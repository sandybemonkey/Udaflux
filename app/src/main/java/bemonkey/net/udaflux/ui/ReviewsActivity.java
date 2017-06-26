package bemonkey.net.udaflux.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import bemonkey.net.udaflux.R;
import bemonkey.net.udaflux.adapters.ReviewAdapter;
import bemonkey.net.udaflux.models.reviews.ReviewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReviewsActivity extends AppCompatActivity {

    @BindView(R.id.rv_reviews_list)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        mUnbinder = ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ReviewModel mReviewResults = getIntent().getExtras().getParcelable("REVIEWS");

        assert mReviewResults != null;
        ReviewAdapter mReviewAdapter = new ReviewAdapter(
                getApplicationContext(),
                mReviewResults.getResults()
        );
        mRecyclerView.setAdapter(mReviewAdapter);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
