package com.firmanjabar.submission4.feature.movie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firmanjabar.submission4.R;
import com.firmanjabar.submission4.data.model.Movie;
import com.firmanjabar.submission4.utils.Utils;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.GridViewHolder> {
    private ArrayList<Movie> listMovie;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick ( Movie movie );
    }

    public MovieAdapter(Context context, OnItemClickListener listener) {
        this.listener = listener;
        listMovie = new ArrayList<Movie>();
    }

    private ArrayList<Movie> getListMovie () {
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> listMovie) {
        this.listMovie = listMovie;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup, false);
        return new GridViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int i) {
        holder.bind(listMovie.get(i), listener);
        Movie movies = getListMovie().get(i);
        holder.movieTitle.setText(movies.getTitle());
        holder.movieDate.setText(movies.getRelease_date());
        holder.movieDesc.setText(movies.getOverview());
        holder.movieRating.setRating((float) (movies.getVote_average() / 2));
    }
    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView movieTitle, movieDate, movieDesc;
        RatingBar movieRating;

        GridViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            movieTitle = itemView.findViewById(R.id.card_title);
            movieDate = itemView.findViewById(R.id.date);
            movieDesc = itemView.findViewById(R.id.desc);
            movieRating = itemView.findViewById(R.id.rate);
        }

        void bind(final Movie movie, final OnItemClickListener listener) {
            Utils.setImage(movie.getPoster_path(), imgPhoto);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }
}
