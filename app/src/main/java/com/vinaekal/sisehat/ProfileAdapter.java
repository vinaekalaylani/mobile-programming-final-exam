// Final ProfileAdapter v8 (Manual Override)
package com.vinaekal.sisehat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends ListAdapter<ProfileItem, ProfileAdapter.ProfileViewHolder> {

    private final Context context;

    public ProfileAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_info, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.bind(getItem(position), context);
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private final ImageView icon;
        private final TextView label;
        private final TextView value;

        ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_icon);
            label = itemView.findViewById(R.id.tv_label);
            value = itemView.findViewById(R.id.tv_value);
        }

        // KUNCI PERBAIKAN: Logika untuk memilih ikon dan background yang berbeda
        void bind(ProfileItem item, Context context) {
            label.setText(item.getLabel());
            value.setText(item.getValue());

            int iconRes = 0;
            int backgroundRes = 0;

            switch (item.getType()) {
                case EMAIL:
                    iconRes = R.drawable.ic_email;
                    backgroundRes = R.drawable.bg_icon_email;
                    break;
                case PHONE:
                    iconRes = R.drawable.ic_phone;
                    backgroundRes = R.drawable.bg_icon_phone;
                    break;
                case BIRTHDATE:
                    iconRes = R.drawable.ic_calendar;
                    backgroundRes = R.drawable.bg_icon_birth;
                    break;
                case ADDRESS:
                    iconRes = R.drawable.ic_location;
                    backgroundRes = R.drawable.bg_icon_address;
                    break;
                case JOB:
                    iconRes = R.drawable.ic_job;
                    backgroundRes = R.drawable.bg_icon_job;
                    break;
            }

            icon.setImageResource(iconRes);
            icon.setBackground(ContextCompat.getDrawable(context, backgroundRes));
        }
    }

    private static final DiffUtil.ItemCallback<ProfileItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ProfileItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ProfileItem oldItem, @NonNull ProfileItem newItem) {
            return oldItem.getType() == newItem.getType();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProfileItem oldItem, @NonNull ProfileItem newItem) {
            return oldItem.getLabel().equals(newItem.getLabel()) && oldItem.getValue().equals(newItem.getValue());
        }
    };
}
