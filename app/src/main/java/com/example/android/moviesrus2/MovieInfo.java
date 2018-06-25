package com.example.android.moviesrus2;

public class MovieInfo {
    String movieTitle;
    String movieImagePath;
    String movieSynopsis;
    String movieRating;
    String movieReleaseDate;
    String movieId;

    public MovieInfo(){

    }

    public MovieInfo(String mTitle, String imagePath, String synopsis, String rating, String releaseDate, String id) {
        this.movieTitle = mTitle;
        this.movieImagePath = imagePath;
        this.movieSynopsis = synopsis;
        this.movieRating = rating;
        this.movieReleaseDate = releaseDate;
        this.movieId = id;
    }
}
