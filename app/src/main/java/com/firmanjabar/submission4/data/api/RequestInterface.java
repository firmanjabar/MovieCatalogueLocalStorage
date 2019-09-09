package com.firmanjabar.submission4.data.api;

import com.firmanjabar.submission4.data.model.MovieDetail;
import com.firmanjabar.submission4.data.model.MovieResponse;
import com.firmanjabar.submission4.data.model.ReviewResponse;
import com.firmanjabar.submission4.data.model.TvDetail;
import com.firmanjabar.submission4.data.model.TvResponse;
import com.firmanjabar.submission4.data.model.VideoResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestInterface {

    @GET("movie/popular")
    Observable<MovieResponse> getMovie (
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    @GET("tv/popular")
    Observable<TvResponse> getTv (
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    @GET("movie/{movie_id}")
    Observable<MovieDetail> getMovieDetail (
            @Path("movie_id") String movie_id,
            @Query("api_key") String api_key
    );

    @GET("movie/{movie_id}/videos")
    Observable<VideoResponse> getMovieVideos (
            @Path("movie_id") String movie_id,
            @Query("api_key") String api_key
    );

    @GET("movie/{movie_id}/reviews")
    Observable<ReviewResponse> getMovieReviews (
            @Path("movie_id") String movie_id,
            @Query("api_key") String api_key
    );

    @GET("search/movie")
    Observable<MovieResponse> searchMovie(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("query") String query
    );

    @GET("tv/{tv_id}")
    Observable<TvDetail> getTvDetail (
            @Path("tv_id") String tv_id,
            @Query("api_key") String api_key
    );

    @GET("tv/{tv_id}/videos")
    Observable<VideoResponse> getTvVideos (
            @Path("tv_id") String tv_id,
            @Query("api_key") String api_key
    );

    @GET("tv/{tv_id}/reviews")
    Observable<ReviewResponse> getTvReviews (
            @Path("tv_id") String tv_id,
            @Query("api_key") String api_key
    );

    @GET("search/tv")
    Observable<TvResponse> searchTv(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("query") String query
    );
}
