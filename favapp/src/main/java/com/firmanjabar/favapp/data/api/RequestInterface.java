package com.firmanjabar.favapp.data.api;

import com.firmanjabar.favapp.data.model.MovieDetail;
import com.firmanjabar.favapp.data.model.ReviewResponse;
import com.firmanjabar.favapp.data.model.TvDetail;
import com.firmanjabar.favapp.data.model.VideoResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestInterface {

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
}