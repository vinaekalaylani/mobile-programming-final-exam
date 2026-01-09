package com.vinaekal.sisehat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.vinaekal.sisehat.util.Session;

public class ProfilActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvPhone, tvBirthDate, tvAddress, tvJob;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        session = new Session(this);

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_value_email);
        tvPhone = findViewById(R.id.tv_value_phone);
        tvBirthDate = findViewById(R.id.tv_value_birth);
        tvAddress = findViewById(R.id.tv_value_address);
        tvJob = findViewById(R.id.tv_value_job);

        loadProfileData();

        MaterialButton editProfileButton = findViewById(R.id.btn_edit_profil);
        MaterialButton logoutButton = findViewById(R.id.btn_keluar);

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfilActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> logout());

        setClickAnimation(editProfileButton);
        setClickAnimation(logoutButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat ulang data saat kembali dari EditProfilActivity
        loadProfileData();
    }

    private void loadProfileData() {
        tvName.setText(session.getUsername());
        
        // Cek jika data di session ada, jika tidak biarkan default
        if (!session.getEmail().isEmpty()) tvEmail.setText(session.getEmail());
        if (!session.getPhone().isEmpty()) tvPhone.setText(session.getPhone());
        if (!session.getBirthDate().isEmpty()) tvBirthDate.setText(session.getBirthDate());
        if (!session.getAddress().isEmpty()) tvAddress.setText(session.getAddress());
        if (!session.getJob().isEmpty()) tvJob.setText(session.getJob());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        session.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
