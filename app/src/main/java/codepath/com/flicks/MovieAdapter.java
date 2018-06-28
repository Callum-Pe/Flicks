package codepath.com.flicks;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.flicks.models.Config;
import codepath.com.flicks.models.Movie;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    ArrayList<Movie> movieList;
    Config config;
    Context con;

    public void setConfig(Config config) {
        this.config = config;
    }

    public MovieAdapter(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
    {


        @Nullable @BindView(R.id.ivPosterImage) ImageView ivPosterImage;
         @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;
        @Nullable @BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movieList.get(position);
                // create intent for the new activity
                Intent intent = new Intent(con, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                con.startActivity(intent);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        con = viewGroup.getContext();
        LayoutInflater li = LayoutInflater.from(con);
        View movieView = li.inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Movie movie = movieList.get(i);
        viewHolder.tvOverview.setText(movie.getOverview());
        viewHolder.tvTitle.setText(movie.getTitle());

        boolean portrait = con.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT;
        String imageUrl;
        if(portrait)
        {
             imageUrl = config.getImageUrl(config.getPosterSize(),movie.getImage_url() );
        }
        else
        {
             imageUrl = config.getImageUrl(config.getBackdropSize(),movie.getBackdropPath());
        }
        int placeholderID = portrait ? R.drawable.placeholder : R.drawable.backdrop;
        ImageView imageView = portrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropImage;

        GlideApp.with(con)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(25, 0))
                .placeholder(placeholderID)
                .error(placeholderID)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
