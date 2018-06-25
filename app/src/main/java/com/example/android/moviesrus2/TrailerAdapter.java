package com.example.android.moviesrus2;

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

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private List<TrailerInfo> movieTrailers = new ArrayList<>();
    private final TrailerAdapterOnClickHandler tClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(TrailerInfo movieClicked);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        tClickHandler = clickHandler;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView trailerTextView;
        public final ImageView imageView;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            trailerTextView = view.findViewById(R.id.tv_trailer_name);
            imageView = view.findViewById(R.id.iv_trailer_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TrailerInfo trailerClicked = movieTrailers.get(adapterPosition);
            tClickHandler.onClick(trailerClicked);
        }
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int trailerId = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attach = false;

        View view = inflater.inflate(trailerId, viewGroup, attach);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        TrailerInfo trailer = movieTrailers.get(position);
        trailerAdapterViewHolder.trailerTextView.setText(trailer.trailerName);
        URL thumbnailUrl = NetworkUtils.buildThumbnailUrl(trailer.trailerKey);
        Picasso.with(trailerAdapterViewHolder.imageView.getContext()).load(thumbnailUrl.toString()).into(trailerAdapterViewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        if (null == movieTrailers) return 0;
        return movieTrailers.size();
    }

    public void setTrailers(List<TrailerInfo> trailers) {
        movieTrailers = trailers;
    }
}
