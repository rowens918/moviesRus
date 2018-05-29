package com.example.android.moviesrus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private List<MovieInfo> movieInfos = new ArrayList<>();
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieInfo movieClicked);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView movieTextView;
        public final ImageView imageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            movieTextView = view.findViewById(R.id.tv_movie_data);
            imageView = view.findViewById(R.id.iv_poster_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieInfo movieClicked = movieInfos.get(adapterPosition);
            mClickHandler.onClick(movieClicked);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int movieId = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attach = false;

        View view = inflater.inflate(movieId, viewGroup, attach);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        MovieInfo movie = movieInfos.get(position);
        URL url = NetworkUtils.buildImageUrl(movie.movieImagePath);
        Picasso.with(movieAdapterViewHolder.imageView.getContext()).load(url.toString()).into(movieAdapterViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == movieInfos) return 0;
        return movieInfos.size();
    }

    public void setMovieInfos(List<MovieInfo> movies) {
        movieInfos = movies;
    }
}
