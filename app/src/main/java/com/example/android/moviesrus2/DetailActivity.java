package com.example.android.moviesrus2;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {
    private RecyclerView tRecyclerView;
    private TrailerAdapter trailerAdapter;
    private String movieID = "0";
    private String moviePoster = "";
    private Button favbutton;
    private Button unfavbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        favbutton = findViewById(R.id.button_favorite);
        unfavbutton = findViewById(R.id.button_removefavorite);
        String sortingMethod = getIntent().getStringExtra("sort");

        if (sortingMethod.equals("favorites")){
            favbutton.setVisibility(View.GONE);
            unfavbutton.setVisibility(View.VISIBLE);
        }

        tRecyclerView = findViewById(R.id.recyclerview_trailers);
        ImageView ivDetailImage = findViewById(R.id.iv_detail_image);
        TextView tvTitle = findViewById(R.id.tv_movie_title);
        TextView tvSynopsis = findViewById(R.id.tv_movie_synopsis);
        TextView tvRating = findViewById(R.id.tv_movie_rating);
        TextView tvReleaseDate = findViewById(R.id.tv_movie_release_date);

        String title = getIntent().getStringExtra("title");
        String poster = getIntent().getStringExtra("imagePath");
        moviePoster = poster;
        String synopsis = getIntent().getStringExtra("synopsis");
        String rating = getIntent().getStringExtra("rating");
        String release = getIntent().getStringExtra("releaseDate");
        String id = getIntent().getStringExtra("id");
        movieID = id;
        String reviewUrl = NetworkUtils.buildReviewUrl(id).toString();
        String videoUrl = NetworkUtils.buildVideoUrl(id).toString();



        URL url = NetworkUtils.buildImageUrl(poster);
        Picasso.with(this).load(url.toString()).into(ivDetailImage);
        tvTitle.setText(title);
        tvRating.setText(rating);
        tvReleaseDate.setText(release);
        tvSynopsis.setText(synopsis);
        new FetchReviewData().execute(reviewUrl);
        new FetchTrailerData().execute(videoUrl);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, 0, false);

        tRecyclerView.setLayoutManager(layoutManager);
        tRecyclerView.setHasFixedSize(true);

        trailerAdapter = new TrailerAdapter(this);

        setTitle("Movies'R'Us: " + title);

    }

    @Override
    public void onClick(TrailerInfo trailerClicked) {
        Uri trailer = Uri.parse(NetworkUtils.buildTrailerUrl(trailerClicked.trailerKey).toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, trailer);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public class FetchReviewData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL reviewUrl = new URL(params[0]);
                String reviewJson = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
                return reviewJson;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String reviewJson) {
            TextView tvReviews = findViewById(R.id.tv_movie_reviews);
            String reviews = JsonUtils.parseReviewData(reviewJson);
            tvReviews.setText(reviews);
        }
    }

    public class FetchTrailerData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL trailerUrl = new URL(params[0]);
                String trailerJson = NetworkUtils.getResponseFromHttpUrl(trailerUrl);
                return trailerJson;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String trailerJson) {

            List<TrailerInfo> trailers = JsonUtils.parseTrailerData(trailerJson);
            trailerAdapter.setTrailers(trailers);
            tRecyclerView.setAdapter(trailerAdapter);

        }
    }

    public void onClickAddFavorite(View view) {
        TextView tvTitle = findViewById(R.id.tv_movie_title);
        TextView tvSynopsis = findViewById(R.id.tv_movie_synopsis);
        TextView tvRating = findViewById(R.id.tv_movie_rating);
        TextView tvReleaseDate = findViewById(R.id.tv_movie_release_date);

        String title = tvTitle.getText().toString();
        String poster = moviePoster;
        String synopsis = tvSynopsis.getText().toString();
        String rating = tvRating.getText().toString();
        String releaseDate = tvReleaseDate.getText().toString();
        String thisMovieId = movieID;

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE_PATH, poster);
        contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, synopsis);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, rating);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, thisMovieId);

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            Toast.makeText(getBaseContext(), title + " added to favorites!", Toast.LENGTH_LONG).show();
        }
        favbutton.setVisibility(View.GONE);
        unfavbutton.setVisibility(View.VISIBLE);
        finish();
    }

    public int onClickRemoveFavorite(View view) {
        Uri uri = MovieContract.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(MovieContract.PATH_FAVORITES)
                .appendPath(MovieContract.MovieEntry.COLUMN_MOVIE_ID)
                .appendPath(movieID)
                .build();
        Log.v("DELETE", "Delete uri: " + uri.toString());
        int unfaved = getContentResolver().delete(uri, null, null);

        unfavbutton.setVisibility(View.GONE);
        favbutton.setVisibility(View.VISIBLE);
        return unfaved;
    }
}
