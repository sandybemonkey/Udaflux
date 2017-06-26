package bemonkey.net.udaflux.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import bemonkey.net.udaflux.R;
import bemonkey.net.udaflux.models.reviews.ReviewResult;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;

/**
 * Created by bmnk on 3/19/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private final List<ReviewResult> mReviews;
    private Context mContext;

    public ReviewAdapter(Context vContext, List<ReviewResult>  vReviewResults) {
        mContext = vContext;
        mReviews = vReviewResults;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.authorView.setText(mReviews.get(holder.getPosition()).getAuthor());
        holder.contentView.setText(mReviews.get(holder.getPosition()).getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_author) TextView authorView;
        @BindView(R.id.tv_content) TextView contentView;

    ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
