package com.example.android.moviesrus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ivDetailImage = findViewById(R.id.iv_detail_image);
        TextView tvTitle = findViewById(R.id.tv_movie_title);
        TextView tvSynopsis = findViewById(R.id.tv_movie_synopsis);
        TextView tvRating = findViewById(R.id.tv_movie_rating);
        TextView tvReleaseDate = findViewById(R.id.tv_movie_release_date);


        String title = getIntent().getStringExtra("title");
        String poster = getIntent().getStringExtra("imagePath");
        String synopsis = getIntent().getStringExtra("synopsis");
        String rating = getIntent().getStringExtra("rating");
        String release = getIntent().getStringExtra("releaseDate");

        URL url = NetworkUtils.buildImageUrl(poster);
        Picasso.with(this).load(url.toString()).into(ivDetailImage);
        tvTitle.setText(title);
        tvRating.setText(rating);
        tvReleaseDate.setText(release);
        tvSynopsis.setText(synopsis);
        setTitle("Movies'R'Us: " + title);
    }
}
