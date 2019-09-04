package com.firmanjabar.submission4.feature.favorite_movie.adapter;

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
import com.firmanjabar.submission4.data.model.Favorite;
import com.firmanjabar.submission4.utils.Utils;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.GridViewHolder> {
    private ArrayList<Favorite> listFavorite;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick ( Favorite favorite );
    }

    public FavoriteMovieAdapter(Context context, OnItemClickListener listener) {
        this.listener = listener;
        listFavorite = new ArrayList<Favorite>();
    }

    private ArrayList<Favorite> getListFavorite () {
        return listFavorite;
    }

    public void setListFavorite(ArrayList<Favorite> listFavorite) {
        this.listFavorite = listFavorite;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup, false);
        return new GridViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int i) {
        holder.bind(listFavorite.get(i), listener);
        Favorite movies = getListFavorite().get(i);
        holder.movieTitle.setText(movies.getTitle());
        holder.movieDate.setText(movies.getRelease_date());
        holder.movieDesc.setText(movies.getOverview());
        holder.movieRating.setRating((float) (movies.getVote_average()/2));
    }
    @Override
    public int getItemCount() {
        return getListFavorite().size();
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

        void bind(final Favorite favorite, final OnItemClickListener listener) {
            Utils.setImage(favorite.getPoster_path(), imgPhoto);
            itemView.setOnClickListener(v -> listener.onItemClick(favorite));
        }
    }
}
