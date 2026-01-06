package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    View imageProfile;
    Button buttonBooking, buttonHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageProfile = findViewById(R.id.imageProfile);
        buttonBooking = findViewById(R.id.buttonBooking);
        buttonHistory = findViewById(R.id.buttonHistory);

        imageProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfilActivity.class));
            finish();
        });

        buttonBooking.setOnClickListener(v -> {
            startActivity(new Intent(this, BookingStep1Activity.class));
            finish();
        });
    }
}