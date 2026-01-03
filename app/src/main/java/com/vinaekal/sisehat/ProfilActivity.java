package com.vinaekal.sisehat;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ImageView backArrow = findViewById(R.id.back_arrow);
        setClickAnimation(backArrow);
        backArrow.setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.profile_items_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setNestedScrollingEnabled(false);

        ProfileAdapter adapter = new ProfileAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.submitList(getProfileData());

        MaterialButton editProfileButton = findViewById(R.id.btn_edit_profil);
        MaterialButton logoutButton = findViewById(R.id.btn_keluar);
        setClickAnimation(editProfileButton);
        setClickAnimation(logoutButton);
    }

    private List<ProfileItem> getProfileData() {
        List<ProfileItem> profileItems = new ArrayList<>();
        profileItems.add(new ProfileItem(ProfileItem.Type.EMAIL, getString(R.string.label_email), getString(R.string.dummy_email)));
        profileItems.add(new ProfileItem(ProfileItem.Type.PHONE, getString(R.string.label_nomor_telepon), getString(R.string.dummy_phone)));
        profileItems.add(new ProfileItem(ProfileItem.Type.BIRTHDATE, getString(R.string.label_tanggal_lahir), getString(R.string.dummy_birthdate)));
        profileItems.add(new ProfileItem(ProfileItem.Type.ADDRESS, getString(R.string.label_alamat), getString(R.string.dummy_address)));
        profileItems.add(new ProfileItem(ProfileItem.Type.JOB, getString(R.string.label_pekerjaan), getString(R.string.dummy_job)));
        return profileItems;
    }

    private void setClickAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateScale(v, 0.97f);
                    break;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                case MotionEvent.ACTION_CANCEL:
                    animateScale(v, 1f);
                    break;
            }
            return true;
        });
    }

    private void animateScale(View view, float scale) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleX.setDuration(150);
        scaleY.setDuration(150);
        scaleX.start();
        scaleY.start();
    }
}
