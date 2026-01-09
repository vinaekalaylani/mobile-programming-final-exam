package com.vinaekal.sisehat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookingSuccessActivity extends AppCompatActivity {

    private TextView tvQueueNumber, tvConsultationTime;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        tvQueueNumber = findViewById(R.id.tvQueueNumber);
        tvConsultationTime = findViewById(R.id.tvConsultationTime);
        btnBack = findViewById(R.id.btnBackToDashboard);

        // Ambil data dari Intent
        String queueNumber = getIntent().getStringExtra("queue_number");
        String hospital = getIntent().getStringExtra("hospital");
        String doctor = getIntent().getStringExtra("doctor");
        String department = getIntent().getStringExtra("department");
        String bookingDate = getIntent().getStringExtra("booking_date");

        tvQueueNumber.setText(queueNumber);

        // Simple estimation: 15 menit slot
        String consultationTime = bookingDate.substring(11,16) + " - " +
                bookingDate.substring(11,16) + " WIB";
        tvConsultationTime.setText(consultationTime);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(BookingSuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}