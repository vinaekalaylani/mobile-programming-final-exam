package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vinaekal.sisehat.util.NotificationHelper;
import com.vinaekal.sisehat.util.Session;

public class MainActivity extends AppCompatActivity {

    TextView welcomeUser;
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

        // Inisialisasi Channel Notifikasi
        NotificationHelper.createNotificationChannel(this);

        welcomeUser = findViewById(R.id.textViewWelcome);
        imageProfile = findViewById(R.id.imageProfile);
        buttonBooking = findViewById(R.id.buttonBooking);
        buttonHistory = findViewById(R.id.buttonHistory);

        Session session = new Session(this);
        String username = session.getUsername();
        welcomeUser.setText("Halo, " + username + "!");

        imageProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfilActivity.class));
        });

        buttonBooking.setOnClickListener(v -> {
            startActivity(new Intent(this, BookingStep1Activity.class));
        });

        buttonHistory.setOnClickListener(v -> {
            // TODO: Implement RiwayatActivity
            // startActivity(new Intent(this, RiwayatActivity.class));
        });
    }
}