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
import com.bilbubble.movie_app.db.entities.Favorite;
import com.bilbubble.movie_app.network.Const;
import com.bilbubble.movie_app.utils.OnItemClick;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<Favorite> favorites;
    private OnItemClick onItemClick;

    public  FavoriteAdapter(List<Favorite> favorites, OnItemClick onItemClick){
        this.favorites = favorites;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_movies,parent,false);
        return new ViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull  FavoriteAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(Const.IMG_URL_200 + favorites.get(position).getImgUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                .into(holder.ivCover);
        holder.tvTitle.setText(favorites.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnItemClick onItemClick;
        ImageView ivCover;
        TextView tvTitle;

        public ViewHolder(@NonNull  View itemView, OnItemClick onItemClick) {
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
