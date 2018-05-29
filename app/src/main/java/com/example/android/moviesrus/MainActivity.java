package com.example.android.moviesrus;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private String sortingMethod = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_main);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, 1, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        movieAdapter = new MovieAdapter(this);
        loadMovieData();
    }

    private void loadMovieData() {
        showMovieDataView();
        new FetchMovieDataTask().execute();
    }

    @Override
    public void onClick(MovieInfo movieClicked) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("title", movieClicked.movieTitle);
        intent.putExtra("imagePath", movieClicked.movieImagePath);
        intent.putExtra("synopsis", movieClicked.movieSynopsis);
        intent.putExtra("rating", movieClicked.movieRating);
        intent.putExtra("releaseDate", movieClicked.movieReleaseDate);
        startActivity(intent);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void reloadData() {
        MovieAdapter oldAdapter = movieAdapter;
        mRecyclerView.swapAdapter(oldAdapter, false);

        loadMovieData();
        mRecyclerView.swapAdapter(movieAdapter, false);
    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = NetworkUtils.buildUrl(sortingMethod);
            try {
                String movieJson = NetworkUtils.getResponseFromHttpUrl(url);
                return movieJson;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String movieJson) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(movieJson != null) {
                showMovieDataView();
                List<MovieInfo> movies = JsonUtils.parseMovieData(movieJson);
                movieAdapter.setMovieInfos(movies);
                mRecyclerView.setAdapter(movieAdapter);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String sorted = sortingMethod;
        if(id == R.id.action_settings) {
            switch(sorted) {
                case "popular":
                    sortingMethod = "top_rated";
                    setTitle("Movies'R'Us: Top Rated");
                    break;
                case "top_rated":
                    sortingMethod = "popular";
                    setTitle("Movies'R'Us: Most Popular");
                    break;
            }
        }
        reloadData();
        return super.onOptionsItemSelected(item);
    }
}
