package com.firmanjabar.submission4.feature.tv_detail.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firmanjabar.submission4.R;
import com.firmanjabar.submission4.data.model.Review;

import java.util.ArrayList;

public class TvReviewAdapter extends RecyclerView.Adapter<TvReviewAdapter.ReviewHolder> {
    private ArrayList<Review> reviewList;
    public TvReviewAdapter () {
        this.reviewList = new ArrayList<>();
    }
    public ArrayList<Review> getReviewList() {
        return reviewList;
    }
    public void setReviewList(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder ( @NonNull ReviewHolder holder, int position ) {
        holder.bind(reviewList.get(position));
    }

    @Override
    public int getItemCount () {
        return reviewList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtReview;

        ReviewHolder ( View itemView ) {
            super(itemView);
            txtName = itemView.findViewById(R.id.name);
            txtReview = itemView.findViewById(R.id.review);
        }

        public void bind ( Review review ) {
            txtName.setText(review.getAuthor());
            txtReview.setText(review.getContent());
        }
    }
}
