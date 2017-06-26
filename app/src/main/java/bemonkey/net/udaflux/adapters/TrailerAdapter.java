package bemonkey.net.udaflux.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import bemonkey.net.udaflux.R;
import bemonkey.net.udaflux.models.trailers.TrailerResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by bmnk on 3/16/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private final List<TrailerResult> mTrailers;
    private Context mContext;

    public TrailerAdapter(Context vContext, List<TrailerResult> vTrailerResults) {
        mContext = vContext;
        mTrailers = vTrailerResults;
    }


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_layout, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.mName.setText(mTrailers.get(position).getName());
        String movieid = mTrailers.get(position).getKey();
        Picasso.with(mContext).load("http://img.youtube.com/vi/" + movieid + "/sddefault.jpg").into(holder.mThumb);
        holder.mThumb.setOnClickListener(v -> {
            Intent chooserIntent = Intent.createChooser(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=" + mTrailers.get(position).getKey())
            ), "Select Application").addFlags(FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(chooserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_trailer) TextView mName;
        @BindView(R.id.iv_thumb) ImageView mThumb;

        TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
