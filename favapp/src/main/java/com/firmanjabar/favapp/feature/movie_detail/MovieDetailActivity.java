package com.firmanjabar.favapp.feature.movie_detail;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firmanjabar.favapp.R;
import com.firmanjabar.favapp.data.model.MovieDetail;
import com.firmanjabar.favapp.data.model.ReviewResponse;
import com.firmanjabar.favapp.data.model.Video;
import com.firmanjabar.favapp.data.model.VideoResponse;
import com.firmanjabar.favapp.feature.movie_detail.adapter.MovieReviewAdapter;
import com.firmanjabar.favapp.feature.movie_detail.adapter.MovieVideoAdapter;
import com.firmanjabar.favapp.utils.Constant;
import com.firmanjabar.favapp.utils.Utils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements MovieVideoAdapter.OnItemClickListener {
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_poster) ImageView iv_poster;
    @BindView(R.id.iv_backdrop_poster) ImageView iv_backdrop_poster;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_genres) TextView tv_genres;
    @BindView(R.id.tv_overview) TextView tv_overview;
    @BindView(R.id.tv_tagline) TextView tv_tagline;
    @BindView(R.id.tv_prod) TextView tv_prod;
    @BindView(R.id.tv_status) TextView tv_status;
    @BindView(R.id.tv_popular) TextView tv_popular;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.tv_vote_average) RatingBar tv_vote_average;
    @BindView(R.id.rv_trailers) RecyclerView rv_trailers;
    @BindView(R.id.rv_review) RecyclerView rv_review;
    @BindView(R.id.fab_fav) FloatingActionButton fab_fav;

    private MovieDetailViewModel viewModel;
    private Observer<MovieDetail> observerMovie;
    private Observer<ReviewResponse> observerReview;
    private Observer<VideoResponse> observerVideo;
    private Observer<Boolean> observerFav;
    private MovieReviewAdapter movieReviewAdapter;
    private MovieVideoAdapter movieVideoAdapter;
    private MovieDetail movieDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar.setVisibility(View.VISIBLE);

        movieReviewAdapter = new MovieReviewAdapter();
        movieVideoAdapter = new MovieVideoAdapter(this);
        LinearLayoutManager videoLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_review.setLayoutManager(reviewLayoutManager);
        rv_trailers.setLayoutManager(videoLayoutManager);

        String id = Objects.requireNonNull(getIntent().getExtras()).getString(EXTRA_MOVIE_ID);

        MovieDetailViewModelFactory viewModelFactory = new MovieDetailViewModelFactory(this, id);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel.class);
        setObserver();
        viewModel.getMovieResponse().observe(this, observerMovie);
        viewModel.getReviewResponse().observe(this, observerReview);
        viewModel.getVideoResponse().observe(this, observerVideo);
        viewModel.getIsFavorite().observe(this, observerFav);

        fab_fav.setOnClickListener(v -> viewModel.changeFavState());
    }

    void setObserver(){
        observerMovie = movieDetail -> {
            progressBar.setVisibility(View.GONE);
            if (movieDetail != null){
                this.movieDetail = movieDetail;
                collapsingToolbarLayout.setTitle(movieDetail.getTitle());
                tv_title.setText(movieDetail.getTitle());
                tv_genres.setText(movieDetail.genreToString());
                tv_tagline.setText(movieDetail.getTagline());
                tv_prod.setText(movieDetail.prodToString());
                tv_status.setText(movieDetail.getStatus());
                tv_popular.setText(movieDetail.getPopularity());
                tv_release_date.setText(movieDetail.getRelease_date());
                tv_overview.setText(movieDetail.getOverview());
                tv_vote_average.setRating((float) (movieDetail.getVote_average() / 2));
                Utils.setBackdropImage(movieDetail.getBackdrop_path(), iv_backdrop_poster);
                Utils.setImage(movieDetail.getPoster_path(), iv_poster);
            }
        };

        observerFav = fav -> {
            fab_fav.setImageResource((fav)? R.drawable.ic_added_to_favorites: R.drawable.ic_add_to_favorites);
        };

        observerReview = reviewResponse -> {
            progressBar.setVisibility(View.GONE);
            if (reviewResponse != null){
                movieReviewAdapter.setReviewList(reviewResponse.getResults());
                rv_review.setAdapter(movieReviewAdapter);
            }
        };

        observerVideo = videoResponse -> {
            progressBar.setVisibility(View.GONE);
            if (videoResponse != null){
                movieVideoAdapter.setVideoList(videoResponse.getResults());
                rv_trailers.setAdapter(movieVideoAdapter);
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
            //                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVideoClick(Video video) {
        try {
            String videoUrl = Constant.YOUTUBE_WATCH + video.getKey();
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            startActivity(myIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK, new Intent());
//        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
