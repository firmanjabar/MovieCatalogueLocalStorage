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

    private Context context;
    private ArrayList<Tv> listTv;
    private ArrayList<Tv> listTvFull;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick ( Tv tv );
    }

    public TvAdapter ( Context context, OnItemClickListener listener ) {
        this.context = context;
        this.listener = listener;
        listTv = new ArrayList<>();
        listTvFull = new ArrayList<>();
    }

    private ArrayList<Tv> getListMovie () {
        return listTv;
    }

    public void setListTv ( ArrayList<Tv> listTv ) {
        this.listTv = listTv;
        listTvFull.clear();
        listTvFull.addAll(listTv);
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder ( @NonNull ViewGroup viewGroup, int i ) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder ( @NonNull GridViewHolder holder, int i ) {
        holder.bind(listTv.get(i), listener);
        Tv tv = getListMovie().get(i);
        holder.tvTitle.setText(tv.getName());
        holder.tvDate.setText(tv.getFirst_air_date());
        holder.tvDesc.setText(tv.getOverview());
        holder.tvRating.setRating((float) (tv.getVote_average() / 2));
    }

    @Override
    public int getItemCount () {
        return getListMovie().size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvTitle, tvDate, tvDesc;
        RatingBar tvRating;

        GridViewHolder ( @NonNull View itemView ) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvTitle = itemView.findViewById(R.id.card_title);
            tvDate = itemView.findViewById(R.id.date);
            tvDesc = itemView.findViewById(R.id.desc);
            tvRating = itemView.findViewById(R.id.rate);
        }

        void bind ( final Tv tv, final OnItemClickListener listener ) {
            Utils.setImage(tv.getPoster_path(), imgPhoto);
            itemView.setOnClickListener(v -> listener.onItemClick(tv));
        }
    }

    public void filter ( String textSearched ) {
        textSearched = textSearched.toLowerCase();
        listTv.clear();
        if (!listTvFull.isEmpty()) {
            if (textSearched.length() != 0) {
                for (Tv tvShow : listTvFull) {
                    if (tvShow.getName().toLowerCase().contains(textSearched)) {
                        listTv.add(tvShow);
                    }
                }
            } else {
                listTv.addAll(listTvFull);
            }
        }
        notifyDataSetChanged();
    }
}
