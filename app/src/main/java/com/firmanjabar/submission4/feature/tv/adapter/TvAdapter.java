package com.firmanjabar.submission4.feature.tv.adapter;

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
import com.firmanjabar.submission4.data.model.Tv;
import com.firmanjabar.submission4.utils.Utils;

import java.util.ArrayList;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.GridViewHolder> {
    private ArrayList<Tv> listMovie;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick ( Tv tv );
    }

    public TvAdapter ( Context context, OnItemClickListener listener) {
        this.listener = listener;
        listMovie = new ArrayList<Tv>();
    }

    private ArrayList<Tv> getListMovie () {
        return listMovie;
    }

    public void setListMovie(ArrayList<Tv> listMovie) {
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
        Tv tv = getListMovie().get(i);
        holder.tvTitle.setText(tv.getName());
        holder.tvDate.setText(tv.getFirst_air_date());
        holder.tvDesc.setText(tv.getOverview());
        holder.tvRating.setRating((float) (tv.getVote_average() / 2));
    }
    @Override
    public int getItemCount() {
        return getListMovie().size();
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

        void bind( final Tv tv, final OnItemClickListener listener) {
            Utils.setImage(tv.getPoster_path(), imgPhoto);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(tv);
                }
            });
        }
    }
}
