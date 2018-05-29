package com.example.android.moviesrus;

public class MovieInfo {
    String movieTitle;
    String movieImagePath;
    String movieSynopsis;
    String movieRating;
    String movieReleaseDate;

    public MovieInfo(){

    }

    public MovieInfo(String mTitle, String imagePath, String synopsis, String rating, String releaseDate) {
        this.movieTitle = mTitle;
        this.movieImagePath = imagePath;
        this.movieSynopsis = synopsis;
        this.movieRating = rating;
        this.movieReleaseDate = releaseDate;
    }
}
