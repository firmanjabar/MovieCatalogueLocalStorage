package com.firmanjabar.submission4.feature.favorite_tv.adapter;

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

public class FavoriteTvAdapter extends RecyclerView.Adapter<FavoriteTvAdapter.GridViewHolder> {
    protected Context context;
    private ArrayList<Favorite> listFavorite;
    private ArrayList<Favorite> listFavoriteFull;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick ( Favorite favorite );
    }

    public FavoriteTvAdapter ( Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        listFavorite = new ArrayList<>();
        listFavoriteFull = new ArrayList<>();
    }

    private ArrayList<Favorite> getListFavorite () {
        return listFavorite;
    }

    public void setListFavorite(ArrayList<Favorite> listFavorite) {
        this.listFavorite = listFavorite;
        listFavoriteFull.clear();
        listFavoriteFull.addAll(listFavorite);
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
        Favorite tv = getListFavorite().get(i);
        holder.tvTitle.setText(tv.getTitle());
        holder.tvDate.setText(tv.getRelease_date());
        holder.tvDesc.setText(tv.getOverview());
        holder.tvRating.setRating((float) (tv.getVote_average()/2));
    }
    @Override
    public int getItemCount() {
        return getListFavorite().size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvTitle, tvDate, tvDesc;
        RatingBar tvRating;

        GridViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvTitle = itemView.findViewById(R.id.card_title);
            tvDate = itemView.findViewById(R.id.date);
            tvDesc = itemView.findViewById(R.id.desc);
            tvRating = itemView.findViewById(R.id.rate);
        }

        void bind(final Favorite favorite, final OnItemClickListener listener) {
            Utils.setImage(favorite.getPoster_path(), imgPhoto);
            itemView.setOnClickListener(v -> listener.onItemClick(favorite));
        }
    }

    public void filter(String textSearched) {
        textSearched = textSearched.toLowerCase();
        listFavorite.clear();
        if (!listFavoriteFull.isEmpty()){
            if (textSearched.length() != 0){
                for (Favorite tvShow: listFavoriteFull) {
                    if (tvShow.getTitle().toLowerCase().contains(textSearched)){
                        listFavorite.add(tvShow);
                    }
                }
            } else {
                listFavorite.addAll(listFavoriteFull);
            }
        }
        notifyDataSetChanged();
    }
}
