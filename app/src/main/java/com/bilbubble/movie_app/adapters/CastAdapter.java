package com.bilbubble.movie_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bilbubble.movie_app.R;
import com.bilbubble.movie_app.models.Casts;
import com.bilbubble.movie_app.network.Const;
import com.bilbubble.movie_app.utils.OnItemClick;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder>{
    private List<Casts> casts;


    public CastAdapter(List<Casts> casts){
        this.casts = casts;

    }

    @NonNull
    @Override
    public CastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_cast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull   CastAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(Const.IMG_URL_200 + casts.get(position)
                        .getProfilePicture())
                .into(holder.civProfile);
        holder.tvActorName.setText(casts.get(position).getName());
        holder.tvCharacterName.setText(casts.get(position).getCharacter());
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civProfile;
        TextView tvActorName, tvCharacterName;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            civProfile = itemView.findViewById(R.id.civ_profile_picture);
            tvActorName = itemView.findViewById(R.id.tv_actor_name);
            tvCharacterName = itemView.findViewById(R.id.tv_character_name);
        }
    }
}
