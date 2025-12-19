package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BookingStep2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_step2);

        Button btnKonfirmasi = findViewById(R.id.btnKonfirmasi);

        btnKonfirmasi.setOnClickListener(v -> {
            // Pindah ke halaman Sukses
            Intent intent = new Intent(BookingStep2Activity.this, BookingSuccessActivity.class);
            startActivity(intent);
        });
    }
}