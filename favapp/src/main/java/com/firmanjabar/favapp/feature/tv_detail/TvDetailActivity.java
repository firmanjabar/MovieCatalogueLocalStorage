package com.firmanjabar.favapp.feature.tv_detail;

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
import com.firmanjabar.favapp.data.model.ReviewResponse;
import com.firmanjabar.favapp.data.model.TvDetail;
import com.firmanjabar.favapp.data.model.Video;
import com.firmanjabar.favapp.data.model.VideoResponse;
import com.firmanjabar.favapp.feature.tv_detail.adapter.TvReviewAdapter;
import com.firmanjabar.favapp.feature.tv_detail.adapter.TvVideoAdapter;
import com.firmanjabar.favapp.utils.Constant;
import com.firmanjabar.favapp.utils.Utils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvDetailActivity extends AppCompatActivity implements TvVideoAdapter.OnItemClickListener{
    public static final String EXTRA_TV_ID = "extra_tv_id";

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_poster) ImageView iv_poster;
    @BindView(R.id.iv_backdrop_poster) ImageView iv_backdrop_poster;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_genres) TextView tv_genres;
    @BindView(R.id.tv_overview) TextView tv_overview;
    @BindView(R.id.tv_created) TextView tv_created;
    @BindView(R.id.tv_prod) TextView tv_prod;
    @BindView(R.id.tv_status) TextView tv_status;
    @BindView(R.id.tv_popular) TextView tv_popular;
    @BindView(R.id.tv_first_air_date) TextView tv_first_air_date;
    @BindView(R.id.tv_last_air_date) TextView tv_last_air_date;
    @BindView(R.id.tv_vote_average) RatingBar tv_vote_average;
    @BindView(R.id.rv_trailers) RecyclerView rv_trailers;
    @BindView(R.id.rv_review) RecyclerView rv_review;
    @BindView(R.id.fab_fav) FloatingActionButton fab_fav;

    private TvDetailViewModel viewModel;
    private Observer<VideoResponse> observerVideo;
    private TvVideoAdapter tvVideoAdapter;
    private Observer<ReviewResponse> observerReview;
    private TvReviewAdapter tvReviewAdapter;
    private Observer<TvDetail> observerTv;
    private Observer<Boolean> observerFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        progressBar.setVisibility(View.VISIBLE);
        tvVideoAdapter = new TvVideoAdapter(this);
        tvReviewAdapter = new TvReviewAdapter();
        LinearLayoutManager videoLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_trailers.setLayoutManager(videoLayoutManager);
        rv_review.setLayoutManager(reviewLayoutManager);

        String id = Objects.requireNonNull(getIntent().getExtras()).getString(EXTRA_TV_ID);

        TvDetailViewModelFactory viewModelFactory = new TvDetailViewModelFactory(this, id);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TvDetailViewModel.class);
        setObserver();
        viewModel.getVideoResponse().observe(this, observerVideo);
        viewModel.getReviewResponse().observe(this, observerReview);
        viewModel.getTvResponse().observe(this, observerTv);
        viewModel.getIsFavorite().observe(this, observerFav);

        fab_fav.setOnClickListener(v -> viewModel.changeFavState());
    }

    void setObserver () {
        observerTv = tvDetail -> {
            progressBar.setVisibility(View.GONE);
            if (tvDetail != null){
                collapsingToolbarLayout.setTitle(tvDetail.getName());
                tv_title.setText(tvDetail.getName());
                tv_genres.setText(tvDetail.genreToString());
                tv_created.setText(tvDetail.createdByToString());
                tv_prod.setText(tvDetail.prodToString());
                tv_status.setText(tvDetail.getStatus());
                tv_popular.setText(tvDetail.getPopularity());
                tv_first_air_date.setText(tvDetail.getFirst_air_date());
                tv_last_air_date.setText(tvDetail.getLast_air_date());
                tv_overview.setText(tvDetail.getOverview());
                tv_vote_average.setRating((float) (tvDetail.getVote_average() / 2));
                Utils.setBackdropImage(tvDetail.getBackdrop_path(), iv_backdrop_poster);
                Utils.setImage(tvDetail.getPoster_path(), iv_poster);
            }
        };
        observerFav = fav -> {
            fab_fav.setImageResource((fav)? R.drawable.ic_added_to_favorites: R.drawable.ic_add_to_favorites);
        };
        observerVideo = videoResponse -> {
            progressBar.setVisibility(View.GONE);
            if (videoResponse != null){
                tvVideoAdapter.setVideoList(videoResponse.getResults());
                rv_trailers.setAdapter(tvVideoAdapter);
            }
        };
        observerReview = reviewResponse -> {
            progressBar.setVisibility(View.GONE);
            if (reviewResponse != null){
                tvReviewAdapter.setReviewList(reviewResponse.getResults());
                rv_review.setAdapter(tvReviewAdapter);
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
    public void onBackPressed() {
        setResult(Activity.RESULT_OK, new Intent());
//        super.onBackPressed();
        finish();
    }

    @Override
    public void onVideoClick ( Video video ) {
        try {
            String videoUrl = Constant.YOUTUBE_WATCH + video.getKey();
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            startActivity(myIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
