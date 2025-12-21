package com.example.homepage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.profile_items_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ProfileItem> profileItems = new ArrayList<>();
        profileItems.add(new ProfileItem(R.drawable.ic_email, R.color.email_icon_background, R.color.email_icon_tint, "Email", "johndoe@email.com"));
        profileItems.add(new ProfileItem(R.drawable.ic_phone, R.color.phone_icon_background, R.color.phone_icon_tint, "Nomor Telepon", "+62 812-3456-7890"));
        profileItems.add(new ProfileItem(R.drawable.ic_date, R.color.date_icon_background, R.color.date_icon_tint, "Tanggal Lahir", "15 Januari 1990"));
        profileItems.add(new ProfileItem(R.drawable.ic_location, R.color.location_icon_background, R.color.location_icon_tint, "Alamat", "Jl. Kesehatan No. 123, Jakarta"));
        profileItems.add(new ProfileItem(R.drawable.ic_work, R.color.work_icon_background, R.color.work_icon_tint, "Pekerjaan", "Karyawan Swasta"));

        ProfileAdapter adapter = new ProfileAdapter(profileItems);
        recyclerView.setAdapter(adapter);
    }
}