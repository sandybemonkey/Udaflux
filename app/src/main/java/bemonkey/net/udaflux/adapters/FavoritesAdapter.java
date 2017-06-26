package bemonkey.net.udaflux.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import bemonkey.net.udaflux.R;
import bemonkey.net.udaflux.db.FavoritesContract;
import bemonkey.net.udaflux.models.movies.MovieResult;
import bemonkey.net.udaflux.ui.DetailsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;

import static bemonkey.net.udaflux.R.string.poster_prefix_url;

/**
 * Created by bmnk on 3/25/17.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    private Context mContext;
    private Cursor mFavorites;

    public FavoritesAdapter(Context vContext, Cursor vFavorites) {
        mContext = vContext;
        mFavorites = vFavorites;
    }

    public void swapCursor(Cursor newCursor){
        mFavorites = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //getting my ref to my poster item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_item, parent, false);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.FavoriteViewHolder holder, int position) {
        int idIndex = mFavorites.getColumnIndex(FavoritesContract.Favorites.COLUMN_MOVIE_ID);
        int titleIndex = mFavorites.getColumnIndex(FavoritesContract.Favorites.COLUMN_TITLE);
        int synopsisIndex = mFavorites.getColumnIndex(FavoritesContract.Favorites.COLUMN_SYNOPSIS);
        int releaseDateIndex = mFavorites.getColumnIndex(FavoritesContract.Favorites.COLUMN_RELEASE_DATE);
        int voteAverageIndex = mFavorites.getColumnIndex(FavoritesContract.Favorites.COLUMN_VOTE_AVERAGE);
        int posterIndex = mFavorites.getColumnIndex(FavoritesContract.Favorites.COLUMN_POSTER_PATH);

        if (mFavorites.moveToPosition(position)){
            int movieId = mFavorites.getInt(idIndex);
            String title = mFavorites.getString(titleIndex);
            String synopsis = mFavorites.getString(synopsisIndex);
            String releaseDate = mFavorites.getString(releaseDateIndex);
            double voteAverage = mFavorites.getDouble(voteAverageIndex);
            String posterPath = mFavorites.getString(posterIndex);

            Picasso.with(mContext).load(mContext.getString(R.string.poster_prefix_url) + posterPath).into(holder.imageView);

            MovieResult movieResult = new MovieResult();
            movieResult.setId(movieId);
            movieResult.setTitle(title);
            movieResult.setOverview(synopsis);
            movieResult.setReleaseDate(releaseDate);
            movieResult.setVoteAverage(voteAverage);
            movieResult.setPosterPath(posterPath);

            holder.imageView.setOnClickListener(v -> {

                Intent i = new Intent(v.getContext(), DetailsActivity.class);
                final Intent intent = i.putExtra("movies", movieResult);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if(null == mFavorites) return 0;
        return mFavorites.getCount();
    }


    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_poster_item)
        ImageView imageView;
        FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
