package com.example.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vinaekal.sisehat.R;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private final List<ProfileItem> items;

    public ProfileAdapter(List<ProfileItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_info, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        ProfileItem item = items.get(position);
        Context context = holder.itemView.getContext();

        holder.icon.setImageResource(item.getIcon());
        holder.icon.setColorFilter(ContextCompat.getColor(context, item.getIconTintColor()));
        holder.iconContainer.setCardBackgroundColor(ContextCompat.getColor(context, item.getIconBackgroundColor()));
        holder.label.setText(item.getLabel());
        holder.value.setText(item.getValue());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        final CardView iconContainer;
        final ImageView icon;
        final TextView label;
        final TextView value;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            iconContainer = itemView.findViewById(R.id.icon_container);
            icon = itemView.findViewById(R.id.iv_icon);
            label = itemView.findViewById(R.id.tv_label);
            value = itemView.findViewById(R.id.tv_value);
        }
    }
}