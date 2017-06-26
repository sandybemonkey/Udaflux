package bemonkey.net.udaflux.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import bemonkey.net.udaflux.ui.DetailsActivity;
import bemonkey.net.udaflux.R;
import bemonkey.net.udaflux.models.movies.MovieResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bmnk on 30/01/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PosterViewHolder>{

    private Context mContext;
    private List<MovieResult> mMovies;

    public MoviesAdapter(Context vContext, List<MovieResult> vMovies){
        mContext = vContext;
        mMovies = vMovies;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //getting my ref to my poster item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_item, parent, false);

        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, final int position) {
        //Getting the prefix to make the call to get each posters
        String mPrefix = mContext.getResources().getString(R.string.poster_prefix_url);

        //Getting each posters
        Picasso.with(mContext).load(mPrefix + mMovies.get(holder.getAdapterPosition()).getPosterPath()).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {

            Intent i = new Intent(v.getContext(), DetailsActivity.class);
            i.putExtra("movies", mMovies.get(holder.getAdapterPosition()));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_poster_item) ImageView imageView;
        PosterViewHolder(View itemView) {
            super(itemView);
            //getting my imageview
            ButterKnife.bind(this, itemView);
        }

    }
}
