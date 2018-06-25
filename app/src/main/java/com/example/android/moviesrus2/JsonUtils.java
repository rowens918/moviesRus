package com.example.android.moviesrus2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String KEY_MOVIE_TITLE = "title";
    private static final String KEY_MOVIE_POSTER_URL_PATH = "poster_path";
    private static final String KEY_MOVIE_SYNOPSIS = "overview";
    private static final String KEY_MOVIE_RATING = "popularity";
    private static final String KEY_MOVIE_RELEASE_DATE = "release_date";
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TRAILER_ID = "id";
    private static final String KEY_TRAILER_KEY = "key";
    private static final String KEY_TRAILER_NAME = "name";

    public static List<MovieInfo> parseMovieData(String rawJson) {
        try {
            JSONObject jsonObject = new JSONObject(rawJson);
            JSONArray results = jsonObject.getJSONArray("results");
            List<MovieInfo> movies = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                MovieInfo tempMovie = new MovieInfo();
                tempMovie.movieTitle = results.getJSONObject(i).optString(KEY_MOVIE_TITLE);
                tempMovie.movieImagePath = results.getJSONObject(i).optString(KEY_MOVIE_POSTER_URL_PATH);
                tempMovie.movieSynopsis = results.getJSONObject(i).optString(KEY_MOVIE_SYNOPSIS);
                tempMovie.movieRating = results.getJSONObject(i).optString(KEY_MOVIE_RATING);
                tempMovie.movieReleaseDate = results.getJSONObject(i).optString(KEY_MOVIE_RELEASE_DATE);
                tempMovie.movieId = results.getJSONObject(i).optString(KEY_MOVIE_ID);
                movies.add(i, tempMovie);
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseReviewData(String reviewData) {
        StringBuilder reviews = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(reviewData);
            JSONArray jsonReviews = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonReviews.length(); i++) {
                reviews.append(System.getProperty("line.separator"))
                        .append(jsonReviews.getJSONObject(i).optString(KEY_AUTHOR))
                        .append(System.getProperty("line.separator"))
                        .append(jsonReviews.getJSONObject(i).optString(KEY_CONTENT))
                        .append(System.getProperty("line.separator"))
                        .append(System.getProperty("line.separator"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews.toString();
    }

    public static List<TrailerInfo> parseTrailerData(String trailerData) {
        List<TrailerInfo> trailers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(trailerData);
            JSONArray jsonTrailers = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonTrailers.length(); i++) {
                TrailerInfo tempTrailer = new TrailerInfo(jsonTrailers.getJSONObject(i).optString(KEY_TRAILER_ID),
                        jsonTrailers.getJSONObject(i).optString(KEY_TRAILER_KEY),
                        jsonTrailers.getJSONObject(i).optString(KEY_TRAILER_NAME));
                trailers.add(i, tempTrailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }
}
