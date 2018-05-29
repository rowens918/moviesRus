package com.example.android.moviesrus;

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
                movies.add(i, tempMovie);
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
