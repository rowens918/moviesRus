package com.example.android.moviesrus2;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {
    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String POPULAR = "popularity.desc";
    public static final String TOP_RATED = "top_rated";
    public static final String SORT_PARAM = "sort_by";
    public static final String API_PARAM = "api_key";
    public static final String YOUTUBE_PARAM = "v";

    //Add your API KEY here, because you do not get to use mine :P
    public final static String API_KEY = "";

    public static URL buildUrl(String Sorted) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(Sorted)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.v("URL", "Built URL: " + url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static URL buildImageUrl(String path) {
        String imageBaseUrl = "http://image.tmdb.org/t/p/original//";
        path = path.replace("/", "");
        Uri builtUri = Uri.parse(imageBaseUrl).buildUpon()
                .appendPath(path)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewUrl(String id) {
        String reviewBaseUrl = "https://api.themoviedb.org/3/movie/";
        Uri builtUri = Uri.parse(reviewBaseUrl).buildUpon()
                .appendPath(id)
                .appendPath("reviews")
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        Log.v("Building", "Review URL: " + builtUri.toString());

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildVideoUrl(String id) {
        String reviewBaseUrl = "https://api.themoviedb.org/3/movie/";
        Uri builtUri = Uri.parse(reviewBaseUrl).buildUpon()
                .appendPath(id)
                .appendPath("videos")
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        Log.v("Building", "Video URL: " + builtUri.toString());

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTrailerUrl(String key) {
        String reviewBaseUrl = "https://www.youtube.com/watch";
        Uri builtUri = Uri.parse(reviewBaseUrl).buildUpon()
                .appendQueryParameter(YOUTUBE_PARAM, key)
                .build();
        Log.v("Building", "Youtube URL: " + builtUri.toString());

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildThumbnailUrl(String key) {
        String reviewBaseUrl = "https://img.youtube.com/vi/";
        Uri builtUri = Uri.parse(reviewBaseUrl).buildUpon()
                .appendPath(key)
                .encodedFragment("/0.jpg")
                .build();
        String builtUrl = builtUri.toString();
        builtUrl = builtUrl.replace("#", "");
        Log.v("Building", "Thumbnail URL: " + builtUrl);

        URL url = null;
        try {
            url = new URL(builtUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
