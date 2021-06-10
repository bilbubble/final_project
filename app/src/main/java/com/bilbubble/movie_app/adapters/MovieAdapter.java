package com.bilbubble.movie_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bilbubble.movie_app.R;
import com.bilbubble.movie_app.models.Movies;
import com.bilbubble.movie_app.network.Const;
import com.bilbubble.movie_app.utils.OnItemClick;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movies> nowPlayings;
    private OnItemClick onItemClick;


    public MovieAdapter(List<Movies> nowPlayings, OnItemClick onItemClick){
        this.nowPlayings = nowPlayings;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_movies, parent, false);
        return new ViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(Const.IMG_URL_ORI + nowPlayings.get(position).getCover())
                .into(holder.ivCover);
        holder.tvTitle.setText(nowPlayings.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return nowPlayings.size();
    }

    public void appendList(List<Movies> extraMovie){
        nowPlayings.addAll(extraMovie);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClick onItemClick;
        ImageView ivCover;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivCover = itemView.findViewById(R.id.iv_show_cover);
            tvTitle = itemView.findViewById(R.id.show_title);
            this.onItemClick = onItemClick;
        }

        @Override
        public void onClick(View view) {
            onItemClick.onClick(getAdapterPosition());
        }
    }
}

